package com.gl.meetsynthbackend.config;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.memory.InMemoryChatMemory;
import org.springframework.ai.embedding.EmbeddingModel;
import org.springframework.ai.reader.TextReader;
import org.springframework.ai.transformer.splitter.TokenTextSplitter;
import org.springframework.ai.vectorstore.SimpleVectorStore;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;

@Configuration
public class AIConfig {

    @Bean
    ChatClient chatClient(ChatClient.Builder builder) {
        return builder.build();
    }

    @Bean
    InMemoryChatMemory inMemoryChatMemory() {
        return new InMemoryChatMemory();
    }

//    @Bean
//    CommandLineRunner ingestTermOfServiceToVectorStore(EmbeddingModel embeddingModel, VectorStore vectorStore,
//                                                       @Value("classpath:rag/terms-of-service.txt") Resource termsOfServiceDocs) {
//
//        return args -> {
//            // Ingest the document into the vector store
//            vectorStore.write(                                  // 3.写入
//                    new TokenTextSplitter().transform(          // 2.转换
//                            new TextReader(termsOfServiceDocs).read())  // 1.读取
//            );
//
//        };
//    }

}

