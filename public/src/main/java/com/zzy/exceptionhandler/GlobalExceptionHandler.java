package com.zzy.exceptionhandler;

import com.zzy.result.Result;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler{

    // 处理所有未捕获的异常  
    @ExceptionHandler(Exception.class)  
    public Result handleAll(Exception ex) {
        return Result.error("捕获了一个异常: "+ex.getMessage());
    }

}