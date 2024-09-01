package com.zzy.feign;

import com.zzy.request.RegisterAndLoginRequest;
import com.zzy.result.Result;
//import jakarta.servlet.http.HttpServletRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient(value = "user")
public interface UserFeignController {
    @PostMapping("register")
    public Result register(@RequestBody RegisterAndLoginRequest registerAndLoginRequest);

    @PostMapping("login")
    public Result login(@RequestBody RegisterAndLoginRequest registerAndLoginRequest);

    @GetMapping("getUsernameByJWT")
    public Result getUsernameByJWT(@RequestHeader(value = "jwt",required = true) String jwt);


}
