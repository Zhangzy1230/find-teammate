package com.zzy.feign;

import com.zzy.dto.MessageDTO;
import com.zzy.request.MessageRequest;
import com.zzy.result.Result;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

import java.util.Date;
import java.util.concurrent.TimeUnit;

@FeignClient(value = "message")
public interface MessageFeignController {
    @PostMapping("setAndAdd")
    public Result setAndAdd(@RequestBody MessageRequest messageRequest,
                            @RequestHeader("jwt") String jwt);
}
