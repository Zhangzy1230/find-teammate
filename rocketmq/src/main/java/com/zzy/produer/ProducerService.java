package com.zzy.produer;

import jakarta.annotation.Resource;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.springframework.beans.factory.annotation.Autowired;  
import org.springframework.stereotype.Service;  
  
@Service  
public class ProducerService {
    private final String EXCEPTION_TOPIC = "exception";
    @Resource
    private RocketMQTemplate rocketMQTemplate;
  
    public void sendMessage(String message) {
        rocketMQTemplate.sendOneWay(EXCEPTION_TOPIC, message);
    }  
}