package com.zzy.controller;

import com.zzy.consumer.ConsumerService;
import com.zzy.dto.MessageDTO;
import com.zzy.feign.UserFeignController;
import com.zzy.produer.ProducerService;
import com.zzy.request.MessageRequest;
import com.zzy.result.Result;
import com.zzy.service.MessageNumService;
import com.zzy.service.MessageService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import org.redisson.api.RList;
import org.redisson.api.RedissonClient;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;

import static com.zzy.consumer.ConsumerService.MESSAGE;

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
    @Resource
    private MessageService messageService;
    @Resource
    private MessageNumService messageNumService;
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
        RList<MessageDTO> list = redissonClient.getList(MESSAGE + username);
        List<MessageDTO> messageDTOS = list.readAll();
        list.removeAll(messageDTOS);
        return Result.ok(messageDTOS);
    }

    @Operation(summary = "查看我与某人的历史消息")
    @GetMapping("history/{targetUsername}")
    public Result history(@RequestHeader("jwt") String jwt,
                          @PathVariable("targetUsername") String targetUsername){
        String username = userFeignController.getUserByJWT(jwt).getData().getUserName();
        return messageService.history(username,targetUsername);
    }

    @Operation(summary = "测试发送消息")
    @PostMapping("setAndAdd")
    public Result setAndAdd(@RequestBody MessageRequest messageRequest,
                            @RequestHeader("jwt") String jwt){
        try {
            MessageDTO messageDTO = new MessageDTO();
            messageDTO.setSendUsername(messageRequest.getSendUsername());
            messageDTO.setReceiveUsername(messageRequest.getReceiveUsername());
            messageDTO.setContent(messageRequest.getContent());
            messageDTO.setSendTime(new Date());
            RList<MessageDTO> list = redissonClient.getList(MESSAGE + messageRequest.getReceiveUsername());
            list.add(messageDTO);
            list.expire(1, TimeUnit.DAYS);
            String uuid = UUID.randomUUID().toString();
            messageService.addMessage(messageRequest,uuid);
            return setAndAdd(uuid,messageRequest);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    public Result setAndAdd(String uuid,MessageRequest messageRequest){
        messageService.setState(uuid);
        messageNumService.add(messageRequest.getSendUsername());
        return Result.ok(null);
    }
}
