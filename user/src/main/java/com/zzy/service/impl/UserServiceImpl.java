package com.zzy.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zzy.domain.User;
import com.zzy.mapper.UserMapper;
import com.zzy.result.Result;
import com.zzy.service.UserService;
import com.zzy.utils.PasswordEncrypterWithoutSalt;
import jakarta.annotation.Resource;
import org.redisson.api.RBucket;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

/**
* @author zzy
* @description 针对表【user(用户表)】的数据库操作Service实现
* @createDate 2024-08-28 16:11:55
*/
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User>
    implements UserService{
    @Resource
    private UserMapper userMapper;
    @Resource
    private RedissonClient redissonClient;
    private static final String REGISTER_KEY = "register:";
    private static final String REGISTER_LOCK = "register_lock:";
    @Override
    public Result register(String username, String password) {
        RBucket<String> bucket = redissonClient.getBucket(REGISTER_KEY + username);
        //查redis，如果有，直接返回
        if(bucket.get() != null){
            return Result.error("用户名已经存在");
        }
        RLock lock = redissonClient.getLock(REGISTER_LOCK + username);
        Result result = null;
        try {
            //使用双端检索，减轻mysql压力
            lock.lock(10, TimeUnit.SECONDS);
            if(bucket.get() != null){
                return Result.error("用户名已经存在");
            }
            //查mysql
            System.out.println("查mysql");
            LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(User::getUserName,username);
            User user = userMapper.selectOne(queryWrapper);
            if(user == null){
                //mysql中没有数据
                user = new User();
                user.setUserName(username);
                user.setPassword(PasswordEncrypterWithoutSalt.generateHashedPassword(password));
                int insert = userMapper.insert(user);
                if(insert == 0){
                    result = Result.error("mysql插入数据失败");
                    return result;
                }else{
                    result = Result.ok(null);
                }
            }else{
                //mysql中有数据
                result = Result.error("用户名已经存在");
            }
            //数据回写redis
            bucket.set("true");
            bucket.expire(1,TimeUnit.DAYS);
        }finally {
            lock.unlock();
        }
        return result;
    }
}




