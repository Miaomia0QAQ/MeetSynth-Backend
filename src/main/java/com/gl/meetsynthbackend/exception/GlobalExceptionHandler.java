package com.gl.meetsynthbackend.exception;

import com.gl.meetsynthbackend.pojo.Result;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.Objects;
import java.util.Optional;

@ControllerAdvice
public class GlobalExceptionHandler {

    // 处理业务异常
    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<Result> handleBusinessException(BusinessException ex) {
        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body(Result.error(ex.getMessage()));
    }

    // 处理参数校验异常
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Result> handleValidationException(MethodArgumentNotValidException ex) {
        String errorMessage = ex.getBindingResult().getAllErrors().stream()
                .map(error -> Optional.ofNullable(error.getDefaultMessage())
                        .orElse("无效参数")) // 处理单个消息为null的情况
                .filter(Objects::nonNull) // 二次过滤
                .findFirst()
                .orElse("参数校验失败"); // 最终保障

        return ResponseEntity.badRequest()
                .body(Result.error(errorMessage));
    }

    // 处理其他未捕获异常（可选增强）
    @ExceptionHandler(Exception.class)
    public ResponseEntity<Result> handleGlobalException(Exception ex) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(Result.error("服务器内部错误"));
    }
}