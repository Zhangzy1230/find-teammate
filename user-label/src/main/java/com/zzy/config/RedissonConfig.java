package com.zzy.config;

import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
//@RefreshScope
public class RedissonConfig {
    @Value("${redis.ip}")
    private String redisIp;
    @Value("${redis.password}")
    private String redisPassword;
    @Value("${redis.connection-pool-size}")
    private int connectionPoolSize;
    @Value("${redis.connection-minimum-idle-size}")
    private int connectionMinimumIdleSize;
    @Value("${redis.connection-timeout}")
    private int connectionTimeout;
    @Bean(destroyMethod="shutdown")
    public RedissonClient redisson() {
        Config config = new Config();
        // 使用单节点模式
//        config.useSingleServer()
//                .setAddress("redis://122.152.215.133:6379") // Redis服务器地址
//                .setPassword("123456"); // 如果Redis服务器设置了密码
        // 使用单节点模式
        config.useSingleServer()
                .setAddress(redisIp)
                .setPassword(redisPassword)
                .setConnectionPoolSize(connectionPoolSize)
                .setConnectionMinimumIdleSize(connectionMinimumIdleSize)
                .setConnectTimeout(connectionTimeout);
        // 或者使用其他模式，如主从复制、哨兵、Redis Cluster等

        return Redisson.create(config);
    }

}