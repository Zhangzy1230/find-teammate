package com.zzy.controller;

import com.alibaba.csp.sentinel.annotation.SentinelResource;
import com.zzy.block.UserBlock;
import com.zzy.domain.User;
import com.zzy.dto.UserDTO;
import com.zzy.request.RegisterAndLoginRequest;
import com.zzy.result.Result;
import com.zzy.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping()
@Tag(name="user")
public class UserController {
    @Resource
    private UserService userService;

    @Operation(summary = "注册用户")
    @PostMapping("register")
    @SentinelResource(value = "register",blockHandlerClass = UserBlock.class,blockHandler = "registerBlockHandler")
    public Result register(@RequestBody RegisterAndLoginRequest registerAndLoginRequest){
        String username = registerAndLoginRequest.getUserName();
        String password = registerAndLoginRequest.getPassword();
        if(!wellForm(username,password)){
            return Result.error("只能由字母和数字组成");
        }
        //布隆过滤器
        if(userService.usernameInBloomFilter(username)){
            return Result.error("用户名存在（布隆过滤器）");
        }
        return userService.register(username,password);
    }
    @Operation(summary = "用户登录")
    @PostMapping("login")
    @SentinelResource(value = "login",blockHandlerClass = UserBlock.class,blockHandler = "loginBlockHandler")
    public Result login(@RequestBody RegisterAndLoginRequest registerAndLoginRequest){
        String username = registerAndLoginRequest.getUserName();
        String password = registerAndLoginRequest.getPassword();
        if(!wellForm(username,password)){
            return Result.error("只能由字母和数字组成");
        }
        //布隆过滤器
        if(!userService.usernameInBloomFilter(username)){
            return Result.error("用户名不存在（布隆过滤器）");
        }
        return userService.login(username,password);
    }

    @Operation(summary = "通过jwt获取用户，jwt不合法时不行")
    @GetMapping("getUserByJWT")
    @SentinelResource(value = "getUsernameByJWT",blockHandlerClass = UserBlock.class,blockHandler = "getUsernameByJWTBlockHandler")
    public Result<UserDTO> getUserByJWT(@RequestHeader(value = "jwt",required = true) String jwt){
        return userService.getUserByJWT(jwt);
    }


    @Operation(summary = "测试全局异常处理器")
    @GetMapping("test/exception")
    @SentinelResource(value = "test/exception",blockHandlerClass = UserBlock.class,blockHandler = "testExceptionBlockHandler")
    public Result testException(){
        int i = 1/0;
        return Result.ok(null);
    }


//    blockHandler / blockHandlerClass: blockHandler 对应处理 BlockException 的函数名称，可选项。
//    blockHandler 函数访问范围需要是 public，返回类型需要与原方法相匹配，参数类型需要和原方法相匹配并且最后加一个额外的参数，类型为 BlockException。
//    blockHandler 函数默认需要和原方法在同一个类中。
//    若希望使用其他类的函数，则可以指定 blockHandlerClass 为对应的类的 Class 对象，注意对应的函数必需为 static 函数，否则无法解析。

//    fallback / fallbackClass：fallback 函数名称，可选项，用于在抛出异常的时候提供 fallback 处理逻辑。fallback 函数可以针对所有类型的异常（除了 exceptionsToIgnore 里面排除掉的异常类型）进行处理。fallback 函数签名和位置要求：
//    返回值类型必须与原函数返回值类型一致；
//    方法参数列表需要和原函数一致，或者可以额外多一个 Throwable 类型的参数用于接收对应的异常。
//    fallback 函数默认需要和原方法在同一个类中。若希望使用其他类的函数，则可以指定 fallbackClass 为对应的类的 Class 对象，注意对应的函数必需为 static 函数，否则无法解析。
    public static boolean wellForm(String username,String password){
        if(username == null || password == null){
            return false;
        }
        String regex = "^[0-9a-zA-Z]+$";
        if(!username.matches(regex)){
            return false;
        }
        if(!password.matches(regex)){
            return false;
        }
        return true;
    }

}
