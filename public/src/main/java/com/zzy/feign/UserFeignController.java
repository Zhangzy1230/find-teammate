package com.zzy.feign;

import com.alibaba.csp.sentinel.annotation.SentinelResource;
import com.zzy.block.UserBlock;
import com.zzy.dto.LabelDTO;
import com.zzy.dto.UserDTO;
import com.zzy.feign.config.UserConfig;
import com.zzy.feign.fallback.UserFallback;
import com.zzy.request.RegisterAndLoginRequest;
import com.zzy.result.Result;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

@FeignClient(value = "user",fallback = UserFallback.class,configuration = UserConfig.class)
public interface UserFeignController {
    @PostMapping("register")
    public Result register(@RequestBody RegisterAndLoginRequest registerAndLoginRequest);

    @PostMapping("login")
    public Result login(@RequestBody RegisterAndLoginRequest registerAndLoginRequest);

    @GetMapping("getUserByJWT")
    public Result<UserDTO> getUserByJWT(@RequestHeader(value = "jwt",required = true) String jwt);

    @GetMapping("test/exception")
    public Result testException();

    @GetMapping("selectByLabelName/{labelName}")
    public Result<LabelDTO> selectByLabelName(@RequestHeader(value = "jwt",required = true) String jwt,
                                              @PathVariable("labelName") String labelName);

    @GetMapping("getUserByUsername/{username}")
    public Result<UserDTO> getUserByUsername(@RequestHeader(value = "jwt",required = true) String jwt,
                                             @PathVariable("username") String username);

    @GetMapping("selectByLabelId/{labelId}")
    public Result<LabelDTO> selectByLabelId(@RequestHeader(value = "jwt",required = true) String jwt,
                                            @PathVariable("labelId") Integer labelId);

    @GetMapping("getUserByUserId/{userId}")
    public Result<UserDTO> getUserByUserId(@RequestHeader(value = "jwt",required = true) String jwt,
                                           @PathVariable("userId") Integer userId);

}
