package com.zzy.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zzy.domain.Message;
import com.zzy.request.MessageRequest;
import com.zzy.service.MessageService;
import com.zzy.mapper.MessageMapper;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.UUID;

/**
* @author zzy
* @description 针对表【message】的数据库操作Service实现
* @createDate 2024-09-13 16:24:10
*/
@Service
public class MessageServiceImpl extends ServiceImpl<MessageMapper, Message>
    implements MessageService{
    @Resource
    private MessageMapper messageMapper;
    @Override
    public void addMessage(MessageRequest messageRequest,String uuid) {
        Message message = new Message();
        message.setUuid(uuid);
        message.setSendUsername(messageRequest.getSendUsername());
        message.setReceiveUsername(messageRequest.getReceiveUsername());
        message.setContent(messageRequest.getContent());
//        message.setSendTime(new Date());
//        message.setState();
        messageMapper.insert(message);
    }

    @Override
    public void setState(String uuid) {
        Message message = new Message();
        message.setUuid(uuid);
        message.setState(1);
        message.setSendTime(new Date());
        messageMapper.updateById(message);
    }
}




