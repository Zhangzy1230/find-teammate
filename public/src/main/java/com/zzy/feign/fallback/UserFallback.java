package com.zzy.feign.fallback;

import com.zzy.feign.UserFeignController;
import com.zzy.request.RegisterAndLoginRequest;
import com.zzy.result.Result;

public class UserFallback implements UserFeignController {
    @Override
    public Result register(RegisterAndLoginRequest registerAndLoginRequest) {
        return Result.error("register fallback");
    }

    @Override
    public Result login(RegisterAndLoginRequest registerAndLoginRequest) {
        return Result.error("login fallback");
    }

    @Override
    public Result getUsernameByJWT(String jwt) {
        return Result.error("getUsernameByJWT fallback");
    }

    @Override
    public Result testException() {
        return Result.error("testException fallback");
    }
}
