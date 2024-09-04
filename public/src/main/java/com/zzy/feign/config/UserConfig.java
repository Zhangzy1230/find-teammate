package com.zzy.feign.config;

import com.zzy.feign.fallback.UserFallback;
import org.springframework.context.annotation.Bean;

public class UserConfig {
    @Bean
    public UserFallback userFallback(){
        return new UserFallback();
    }
}
