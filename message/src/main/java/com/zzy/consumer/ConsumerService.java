package com.zzy.consumer;

import com.zzy.dto.MessageDTO;
import com.zzy.produer.ProducerService;
import com.zzy.request.MessageRequest;
import com.zzy.service.MessageNumService;
import com.zzy.service.MessageService;
import jakarta.annotation.Resource;
import org.apache.rocketmq.spring.annotation.ConsumeMode;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;  
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.redisson.api.RList;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Service;

import java.io.*;
import java.time.LocalDate;
import java.util.Date;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Service  
//@RocketMQMessageListener(topic = "exception", consumerGroup = "my-consumer-group", selectorExpression = "*", consumeMode = ConsumeMode.CONCURRENTLY)
@RocketMQMessageListener(topic = ProducerService.MESSAGE_TOPIC, consumerGroup = "my-consumer-group")
public class ConsumerService implements RocketMQListener<MessageRequest> {
    public static final String MESSAGE = "message:";
    @Resource
    private MessageService messageService;
    @Resource
    private MessageNumService messageNumService;
    @Resource
    private RedissonClient redissonClient;
    @Override  
    public void onMessage(MessageRequest messageRequest) {
        System.out.println("收到消息");
        String uuid = UUID.randomUUID().toString();
        messageService.addMessage(messageRequest,uuid);
        MessageDTO messageDTO = new MessageDTO();
        messageDTO.setSendUsername(messageRequest.getSendUsername());
        messageDTO.setReceiveUsername(messageRequest.getReceiveUsername());
        messageDTO.setContent(messageRequest.getContent());
        messageDTO.setSendTime(new Date());
        setAndAdd(uuid, messageRequest.getSendUsername(),messageDTO, messageRequest.getReceiveUsername());
    }

    //分布式事务seata
    public void setAndAdd(String uuid,String username,MessageDTO messageDTO,String receiveUsername){
        RList<MessageDTO> list = redissonClient.getList(MESSAGE + receiveUsername);
        list.add(messageDTO);
        list.expire(1, TimeUnit.DAYS);
        messageService.setState(uuid);
        messageNumService.add(username);
    }
}