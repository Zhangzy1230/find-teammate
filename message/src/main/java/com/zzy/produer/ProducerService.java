package com.zzy.produer;

import com.zzy.request.MessageRequest;
import jakarta.annotation.Resource;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.springframework.stereotype.Service;
  
@Service  
public class ProducerService {
    public static final String MESSAGE_TOPIC = "message_topic";
    public static final String MESSAGE_TAG = "message_tag";
    @Resource
    private RocketMQTemplate rocketMQTemplate;
    public void sendMessage(MessageRequest messageRequest) {
        // 发送消息
        rocketMQTemplate.convertAndSend(MESSAGE_TOPIC, messageRequest);
    }
}