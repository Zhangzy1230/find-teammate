package com.zzy.block;

import com.alibaba.csp.sentinel.slots.block.BlockException;
import com.zzy.request.RegisterAndLoginRequest;
import com.zzy.result.Result;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

public class UserBlock {
//    blockHandler / blockHandlerClass: blockHandler 对应处理 BlockException 的函数名称，可选项。
//    blockHandler 函数访问范围需要是 public，返回类型需要与原方法相匹配，参数类型需要和原方法相匹配并且最后加一个额外的参数，类型为 BlockException。
//    blockHandler 函数默认需要和原方法在同一个类中。
//    若希望使用其他类的函数，则可以指定 blockHandlerClass 为对应的类的 Class 对象，注意对应的函数必需为 static 函数，否则无法解析。

    public static Result registerBlockHandler(@RequestBody RegisterAndLoginRequest registerAndLoginRequest, BlockException blockException) {
        return Result.error("registerBlockHandler");
    }
    public static Result loginBlockHandler(@RequestBody RegisterAndLoginRequest registerAndLoginRequest, BlockException blockException){
        return Result.error("loginBlockHandler");
    }
    public static Result getUsernameByJWTBlockHandler(@RequestHeader(value = "jwt",required = true) String jwt, BlockException blockException){
        return Result.error("getUsernameByJWTBlockHandler");
    }
    public static Result testExceptionBlockHandler(BlockException blockException){
        return Result.error("testExceptionBlockHandler");
    }

}
