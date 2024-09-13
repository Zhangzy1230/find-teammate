package com.zzy.produer;

import com.zzy.request.MessageRequest;
import jakarta.annotation.Resource;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Service;
  
@Service  
public class ProducerService {
    public static final String MESSAGE_TOPIC = "message";
    @Resource
    private RocketMQTemplate rocketMQTemplate;
  
    public void sendMessage(MessageRequest messageRequest) {
//        rocketMQTemplate.sendOneWay(MESSAGE_TOPIC, message);
        rocketMQTemplate.sendOneWay(MESSAGE_TOPIC,messageRequest);
    }
}