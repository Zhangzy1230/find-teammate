package com.zzy.service;

import com.zzy.domain.MessageNum;
import com.baomidou.mybatisplus.extension.service.IService;

/**
* @author zzy
* @description 针对表【message_num】的数据库操作Service
* @createDate 2024-09-13 16:10:03
*/
public interface MessageNumService extends IService<MessageNum> {
    void add(String username);
}
