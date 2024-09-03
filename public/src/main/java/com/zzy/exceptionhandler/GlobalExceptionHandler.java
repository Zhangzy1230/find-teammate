package com.zzy.exceptionhandler;

import com.zzy.result.Result;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler{
  
    // 处理所有未捕获的异常  
    @ExceptionHandler(Exception.class)  
    public Result handleAll(Exception ex) {
        System.out.println("捕获了一个异常: "+ex.getMessage());
        // 这里可以记录日志等操作  
        return Result.error("捕获了一个异常: "+ex.getMessage());
    }

}