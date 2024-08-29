package com.zzy.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zzy.domain.User;
import com.zzy.result.Result;

/**
* @author zzy
* @description 针对表【user(用户表)】的数据库操作Service
* @createDate 2024-08-28 16:11:55
*/
public interface UserService extends IService<User> {
    Result register(String username, String password);
}
