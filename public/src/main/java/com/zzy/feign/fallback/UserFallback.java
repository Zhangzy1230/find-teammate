package com.zzy.feign.fallback;

import com.zzy.dto.LabelDTO;
import com.zzy.dto.UserDTO;
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
    public Result getUserByJWT(String jwt) {
        return Result.error("getUsernameByJWT fallback");
    }

    @Override
    public Result testException() {
        return Result.error("testException fallback");
    }

    @Override
    public Result<LabelDTO> selectByLabelName(String jwt,String labelName) {
        return Result.error("selectByLabelName fallback");
    }

    @Override
    public Result<UserDTO> getUserByUsername(String jwt, String username) {
        return Result.error("getUserByUsername fallback");
    }

    @Override
    public Result<LabelDTO> selectByLabelId(String jwt, Integer labelId) {
        return Result.error("selectByLabelId fallback");
    }
}
