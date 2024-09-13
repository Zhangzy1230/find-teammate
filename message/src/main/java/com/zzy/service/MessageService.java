package com.zzy.service;

import com.zzy.domain.Message;
import com.baomidou.mybatisplus.extension.service.IService;
import com.zzy.request.MessageRequest;

/**
* @author zzy
* @description 针对表【message】的数据库操作Service
* @createDate 2024-09-13 16:24:10
*/
public interface MessageService extends IService<Message> {
    void addMessage(MessageRequest messageRequest,String uuid);
    void setState(String uuid);
}
