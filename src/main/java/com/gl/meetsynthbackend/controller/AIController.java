package com.gl.meetsynthbackend.controller;

import lombok.SneakyThrows;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor;
import org.springframework.ai.chat.client.advisor.QuestionAnswerAdvisor;
import org.springframework.ai.chat.client.advisor.VectorStoreChatMemoryAdvisor;
import org.springframework.ai.chat.memory.InMemoryChatMemory;
import org.springframework.ai.document.Document;
import org.springframework.ai.reader.tika.TikaDocumentReader;
import org.springframework.ai.transformer.splitter.TokenTextSplitter;
import org.springframework.ai.vectorstore.SearchRequest;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.MediaType;
import org.springframework.http.codec.ServerSentEvent;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import reactor.core.publisher.Flux;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/ai")
@CrossOrigin("/*")
public class AIController {

    @Autowired
    private ChatClient chatClient;

    @Autowired
    private InMemoryChatMemory inMemoryChatMemory;

    @Autowired
    private VectorStore vectorStore;

    @CrossOrigin(
            origins = "*",
            allowedHeaders = "*",
            exposedHeaders = "Content-Type",
            methods = {RequestMethod.GET}
    )
    @GetMapping(value = "/summary", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<ServerSentEvent<String>> stream(@RequestParam(value = "message", defaultValue = "讲个笑话") String message) {
        String promptWithContext = """
                你现在是MeetingSynth项目中的智能会议总结助手，负责接收语音识别出来的文字内容，对语音识别出的文字进行修复和提炼，最终对会议内容进行一个总结。
                注意：1.你只需要对会议内容进行总结，会议和资料没有提到的事情不必进行描述
                2.有时，用户会提前输入会议资料，如果会议资料与其内容有关，你可以配合会议资料对会议的内容进行总结。如果无关，请不必要提及。
                下面是会议资料的内容
                ---------------------
                {question_answer_context}
                ---------------------
                """;

        return chatClient.prompt()
                .advisors(
//                        new MessageChatMemoryAdvisor(inMemoryChatMemory),
                        new QuestionAnswerAdvisor(vectorStore, SearchRequest.defaults(), promptWithContext)
                )
                .user(message)
                .stream()
                .content()
                .map(chatResponse -> ServerSentEvent.builder(chatResponse)
                        .event("message")
                        .build())
                .concatWithValues(
                        ServerSentEvent.builder("event: end")
                                .event("message")
                                .build()
                );
    }

    @CrossOrigin(
            origins = "*",
            allowedHeaders = "*",
//            exposedHeaders = "Content-Type",
            methods = {RequestMethod.POST}
    )
    @PostMapping("embedding")
    public Boolean embedding(@RequestParam MultipartFile file) throws IOException {
        // 从IO流中读取文件
        TikaDocumentReader tikaDocumentReader = new TikaDocumentReader(new InputStreamResource(file.getInputStream()));
        // 将文本内容划分成更小的块
        List<Document> splitDocuments = new TokenTextSplitter()
                .apply(tikaDocumentReader.read());
        // 存入向量数据库，这个过程会自动调用embeddingModel,将文本变成向量再存入。
        vectorStore.add(splitDocuments);
        return true;
    }

    @GetMapping("query")
    public List<Document> query(@RequestParam String query) {
        return vectorStore.similaritySearch(query);
    }
}

