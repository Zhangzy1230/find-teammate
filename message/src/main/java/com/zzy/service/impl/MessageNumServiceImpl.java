package com.zzy.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zzy.domain.MessageNum;
import com.zzy.service.MessageNumService;
import com.zzy.mapper.MessageNumMapper;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
* @author zzy
* @description 针对表【message_num】的数据库操作Service实现
* @createDate 2024-09-13 16:10:03
*/
@Service
public class MessageNumServiceImpl extends ServiceImpl<MessageNumMapper, MessageNum>
    implements MessageNumService{
    @Resource
    private MessageNumMapper messageNumMapper;

    @Override
    public void add(String username) {
        LambdaQueryWrapper<MessageNum> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(MessageNum::getUsername,username);
        MessageNum messageNum = messageNumMapper.selectOne(queryWrapper);
        if(messageNum == null){
            messageNum = new MessageNum();
            messageNum.setUsername(username);
            messageNumMapper.insert(messageNum);
        }else{
            messageNum.setNum(messageNum.getNum()+1);
            messageNum.setUpdateTime(new Date());
            messageNumMapper.update(messageNum,queryWrapper);
        }
    }
}




