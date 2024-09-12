package com.zzy.feign;

import com.zzy.feign.config.RocketMQConfig;
import com.zzy.feign.fallback.RocketMQFallback;
import com.zzy.request.LogRequest;
import com.zzy.result.Result;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(value = "rocketmq-server",fallback = RocketMQConfig.class,configuration = RocketMQFallback.class)
public interface RocketMQFeignController {
    @PostMapping("logHandler")
    public Result logHandler(@RequestBody LogRequest logRequest);
}
