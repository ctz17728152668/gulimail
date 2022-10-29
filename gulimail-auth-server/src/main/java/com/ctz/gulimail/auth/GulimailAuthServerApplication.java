package com.ctz.gulimail.auth;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.session.data.redis.config.annotation.web.http.EnableRedisHttpSession;

//整合redis 作为session存储
@EnableRedisHttpSession
@SpringBootApplication
@EnableDiscoveryClient
@EnableFeignClients(basePackages = "com.ctz.gulimail.auth.feign")
public class GulimailAuthServerApplication {

    public static void main(String[] args) {
        SpringApplication.run(GulimailAuthServerApplication.class, args);
    }

}
