package com.zzy;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;


@SpringBootApplication
@EnableFeignClients
@MapperScan("com.zzy.mapper")
public class User {
    public static void main(String[] args) {
        SpringApplication.run(User.class);
//        System.out.println("Hello world!");
    }
}