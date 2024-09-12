package com.zzy.consumer;

import org.apache.rocketmq.spring.annotation.ConsumeMode;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;  
import org.apache.rocketmq.spring.core.RocketMQListener;  
import org.springframework.stereotype.Service;

import java.io.*;
import java.time.LocalDate;

@Service  
//@RocketMQMessageListener(topic = "exception", consumerGroup = "my-consumer-group", selectorExpression = "*", consumeMode = ConsumeMode.CONCURRENTLY)
@RocketMQMessageListener(topic = "exception", consumerGroup = "my-consumer-group")
public class ConsumerService implements RocketMQListener<String> {
    private final String LOG_PATH = "D:\\java项目\\找队友\\find-teammate\\logs\\";
    @Override  
    public void onMessage(String message) {
        // 指定要写入的文件路径
        String filePath = LOG_PATH+LocalDate.now()+".txt";
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
            // 写入字符串
            writer.write(message);
            // 追加换行符和字符串
            writer.newLine();
            //flush()在这里不是必需的
        } catch (IOException e) {
            e.printStackTrace();
        }
    }  
}