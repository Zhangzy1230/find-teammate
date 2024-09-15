package com.zzy.mapper;

import com.zzy.domain.Message;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
* @author zzy
* @description 针对表【message】的数据库操作Mapper
* @createDate 2024-09-13 16:24:10
* @Entity generator.domain.Message
*/
public interface MessageMapper extends BaseMapper<Message> {
    List<Message> history(@Param("username1") String username1,@Param("username2") String username2);
    void deleteAll();
}




