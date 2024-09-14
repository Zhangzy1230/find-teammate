package com.zzy.config;

import org.apache.rocketmq.spring.core.RocketMQTemplate;
//import org.apache.rocketmq.spring.converter
import org.springframework.context.annotation.Bean;  
import org.springframework.context.annotation.Configuration;  
  
//@Configuration
public class RocketMQConfig {  
  
//    @Bean
    public RocketMQTemplate rocketMQTemplate(org.apache.rocketmq.client.producer.DefaultMQProducer defaultMQProducer,  
                                             org.apache.rocketmq.client.consumer.DefaultLitePullConsumer defaultLitePullConsumer) {  
        RocketMQTemplate template = new RocketMQTemplate();  
        template.setProducer(defaultMQProducer);  
        template.setConsumer(defaultLitePullConsumer); // 注意：这里可能需要根据你的实际消费者类型来调整  
        // 设置消息转换器为Jackson2JsonMessageConverter  
//        template.setMessageConverter(new Jackson2JsonMessageConverter());
        return template;  
    }  
}