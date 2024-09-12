package com.zzy.controller;

import com.zzy.produer.ProducerService;
import com.zzy.request.LogRequest;
import com.zzy.result.Result;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping()
@Tag(name = "rocketmq-server")
public class RocketMQController {
    @Resource
    private ProducerService producerService;
    @Operation(summary = "测试日志")
    @PostMapping("logHandler")
    public Result logHandler(@RequestBody LogRequest logRequest){
        producerService.sendMessage(logRequest.getMessage());
        return Result.ok(null);
    }
}
