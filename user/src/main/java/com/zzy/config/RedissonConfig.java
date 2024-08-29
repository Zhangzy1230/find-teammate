package com.zzy.config;

import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RedissonConfig {

    @Bean(destroyMethod="shutdown")
    public RedissonClient redisson() {
        Config config = new Config();
        // 使用单节点模式
        config.useSingleServer()
                .setAddress("redis://122.152.215.133:6379") // Redis服务器地址
                .setPassword("123456"); // 如果Redis服务器设置了密码

        // 或者使用其他模式，如主从复制、哨兵、Redis Cluster等

        return Redisson.create(config);
    }
}