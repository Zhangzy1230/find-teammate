package com.zzy.consumer;

import com.zzy.dto.MessageDTO;
import com.zzy.produer.ProducerService;
import com.zzy.request.MessageRequest;
import com.zzy.result.Result;
import com.zzy.service.MessageNumService;
import com.zzy.service.MessageService;
import jakarta.annotation.Resource;
import org.apache.rocketmq.spring.annotation.ConsumeMode;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;  
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.redisson.api.RList;
import org.redisson.api.RedissonClient;
import org.apache.rocketmq.common.message.Message;
import org.springframework.stereotype.Service;

import java.io.*;
import java.time.LocalDate;
import java.util.Date;
import java.util.Random;
import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;

@Service  
@RocketMQMessageListener(topic = ProducerService.MESSAGE_TOPIC, consumerGroup = "my-consumer-group", consumeMode = ConsumeMode.CONCURRENTLY)
//@RocketMQMessageListener(topic = ProducerService.MESSAGE_TOPIC, consumerGroup = "my-consumer-group", selectorExpression = "*", consumeMode = ConsumeMode.CONCURRENTLY)
public class ConsumerService implements RocketMQListener<MessageRequest> {
    public static final String MESSAGE = "message:";
    @Resource
    private RedissonClient redissonClient;
    @Resource
    private MessageService messageService;
    @Resource
    private MessageNumService messageNumService;
    @Override
    public void onMessage(MessageRequest messageRequest) {
        System.out.println("收到消息"+LocalDate.now().toString());
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
            setAndAdd(uuid,messageRequest);
        } catch (Exception e) {
            return;
        }
    }

    public Result setAndAdd(String uuid, MessageRequest messageRequest){
        messageService.setState(uuid);
        messageNumService.add(messageRequest.getSendUsername());
        return Result.ok(null);
    }
}