package com.zzy.feign.config;

import com.zzy.feign.fallback.RocketMQFallback;
import org.springframework.context.annotation.Bean;

public class RocketMQConfig {
    @Bean
    public RocketMQFallback rocketMQFallback(){
        return new RocketMQFallback();
    }
}
