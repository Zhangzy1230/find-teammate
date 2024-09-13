package com.zzy.controller;

import com.zzy.consumer.ConsumerService;
import com.zzy.dto.MessageDTO;
import com.zzy.feign.UserFeignController;
import com.zzy.produer.ProducerService;
import com.zzy.request.MessageRequest;
import com.zzy.result.Result;
import com.zzy.service.MessageService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import org.redisson.api.RList;
import org.redisson.api.RedissonClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping()
@Tag(name = "message")
public class MessageController {
    @Resource
    private ProducerService producerService;
    @Resource
    private RedissonClient redissonClient;
    @Resource
    private UserFeignController userFeignController;
    @Operation(summary = "发送消息")
    @PostMapping("send")
    public Result send(@RequestBody MessageRequest messageRequest,
                       @RequestHeader("jwt") String jwt){
        messageRequest.setSendUsername(userFeignController.getUserByJWT(jwt).getData().getUserName());
        producerService.sendMessage(messageRequest);
        return Result.ok(null);
    }

    @Operation(summary = "拉取消息")
    @GetMapping("pull")
    public Result pull(@RequestHeader("jwt") String jwt){
        String username = userFeignController.getUserByJWT(jwt).getData().getUserName();
        RList<MessageDTO> list = redissonClient.getList(ConsumerService.MESSAGE + username);
        List<MessageDTO> messageDTOS = list.readAll();
        list.removeAll(messageDTOS);
        return Result.ok(messageDTOS);
    }
}
