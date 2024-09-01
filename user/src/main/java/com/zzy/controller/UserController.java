package com.zzy.controller;

import com.zzy.request.RegisterAndLoginRequest;
import com.zzy.result.Result;
import com.zzy.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("user")
@Tag(name="user模块")
public class UserController {
    @Resource
    private UserService userService;

    @Operation(summary = "注册用户")
    @PostMapping("register")
    public Result register(@RequestBody RegisterAndLoginRequest registerAndLoginRequest){
        String username = registerAndLoginRequest.getUserName();
        String password = registerAndLoginRequest.getPassword();
        if(!wellForm(username,password)){
            return Result.error("只能由字母和数字组成");
        }
        return userService.register(username,password);
    }
    //TODO:布隆过滤器，筛查用户名
    @Operation(summary = "用户登录")
    @PostMapping("login")
    public Result login(@RequestBody RegisterAndLoginRequest registerAndLoginRequest){
        String username = registerAndLoginRequest.getUserName();
        String password = registerAndLoginRequest.getPassword();
        if(!wellForm(username,password)){
            return Result.error("只能由字母和数字组成");
        }
        return userService.login(username,password);
    }

    @Operation(summary = "通过jwt获取用户名，jwt不合法时不行")
    @GetMapping("getUsernameByJWT")
    public Result getUsernameByJWT(@RequestHeader(value = "jwt",required = true) String jwt){
        return userService.getUsernameByJWT(jwt);
    }


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
