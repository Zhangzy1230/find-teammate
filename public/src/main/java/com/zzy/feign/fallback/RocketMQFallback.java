package com.zzy.feign.fallback;

import com.zzy.feign.RocketMQFeignController;
import com.zzy.request.LogRequest;
import com.zzy.result.Result;

public class RocketMQFallback implements RocketMQFeignController {

    @Override
    public Result logHandler(LogRequest logRequest) {
        return Result.error("logHandler fallback");
    }
}
