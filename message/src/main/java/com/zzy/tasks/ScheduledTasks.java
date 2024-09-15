package com.zzy.tasks;

import com.zzy.mapper.MessageMapper;
import jakarta.annotation.Resource;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class ScheduledTasks {  
    @Resource
    private MessageMapper messageMapper;
    @Scheduled(cron = "0 * * * * ?")
    public void reportCurrentTime() {  
        messageMapper.deleteAll();
    }  
}