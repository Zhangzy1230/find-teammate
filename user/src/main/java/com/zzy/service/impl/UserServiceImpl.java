package com.zzy.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zzy.domain.User;
import com.zzy.mapper.UserMapper;
import com.zzy.result.Result;
import com.zzy.service.UserService;
import com.zzy.utils.JwtGenerator;
import com.zzy.utils.PasswordEncrypterWithoutSalt;
import jakarta.annotation.Resource;
import org.redisson.api.RBloomFilter;
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
    private static final String LOGIN_KEY = "login:";
    private static final String PASSWORD_KEY = "password:";
    private static final String REGISTER_LOCK = "register_lock:";
    private static final String USERNAME_BLOOM = "username:bloom";
    private static RBloomFilter<String> usernameBloomFilter;

    @Override
    public Result register(String username, String password) {
        RBucket<String> bucket = redissonClient.getBucket(REGISTER_KEY + username);
        //查redis，如果有，直接返回
        if(bucket.get() != null){
            return Result.error("用户名已经存在（redis）");
        }
        RLock lock = redissonClient.getLock(REGISTER_LOCK + username);
        Result result = null;
        try {
            //使用双端检索，减轻mysql压力
            lock.lock(10, TimeUnit.SECONDS);
            if(bucket.get() != null){
                return Result.error("用户名已经存在（redis）");
            }
            //查mysql
//            System.out.println("查mysql");
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
                    addUsernameToBloomFilter(username);
                    result = Result.ok(null);
                }
            }else{
                //mysql中有数据
                result = Result.error("用户名已经存在（mysql）");
            }
            //数据回写redis
            bucket.set("true");
            bucket.expire(1,TimeUnit.DAYS);
        }finally {
            lock.unlock();
        }
        return result;
    }
    //登录，重新登录直接返回redis里面的值
    @Override
    public Result login(String username, String password) {
        password = PasswordEncrypterWithoutSalt.generateHashedPassword(password);
        RBucket<String> passwordBucket = redissonClient.getBucket(PASSWORD_KEY + username);
        String passwordBucketValue = passwordBucket.get();
        if(passwordBucketValue == null){
            //查mysql
            LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(User::getUserName,username);
            User user = userMapper.selectOne(queryWrapper);
            if(user == null){
                return Result.error("用户名不存在（mysql）");
            }
            //查到后，写回redis
            passwordBucket.set(user.getPassword());
            passwordBucket.expire(1,TimeUnit.DAYS);
            passwordBucketValue = user.getPassword();
        }
        if(!password.equals(passwordBucketValue)){
            return Result.error("密码错误");
        }
        RBucket<String> loginBucket = redissonClient.getBucket(LOGIN_KEY + username);
        String jwt = loginBucket.get();
        if(jwt != null) {
            return Result.ok(jwt);
        }
        //创建jwt
        jwt = JwtGenerator.createToken(username);
        loginBucket.set(jwt);
        loginBucket.expire(1,TimeUnit.DAYS);
        return Result.ok(jwt);
    }

    @Override
    public Result getUsernameByJWT(String jwt) {
        try {
            String username = JwtGenerator.parseUsername(jwt);
            RBucket<String> loginBucket = redissonClient.getBucket(LOGIN_KEY + username);
            String nowJWT = loginBucket.get();
            if(!nowJWT.equals(jwt)){
                return Result.error("未登录或者登录状态异常");
            }
            return Result.ok(username);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return Result.error("未登录或者登录状态异常");
    }

    @Override
    public boolean usernameInBloomFilter(String username) {
        if(usernameBloomFilter == null){
            createUsernameBloomFilter();
        }
        return usernameBloomFilter.contains(username);
    }

    @Override
    public boolean addUsernameToBloomFilter(String username) {
        if(usernameBloomFilter == null){
            createUsernameBloomFilter();
        }
        return usernameBloomFilter.add(username);
    }

    public synchronized void createUsernameBloomFilter(){
        if(usernameBloomFilter != null){
            return;
        }
        usernameBloomFilter = redissonClient.getBloomFilter(USERNAME_BLOOM);
        //这里我们约定好，先写死
        usernameBloomFilter.tryInit(10000L,0.1);
//        usernameBloomFilter.add("admin");
//        usernameBloomFilter.add("zzy");
//        usernameBloomFilter.add("zzz");
//        usernameBloomFilter.add("zzzz");
//        usernameBloomFilter.add("zzy123");

    }

}




