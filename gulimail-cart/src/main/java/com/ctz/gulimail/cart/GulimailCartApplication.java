package com.ctz.gulimail.cart;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.session.data.redis.config.annotation.web.http.EnableRedisHttpSession;

@EnableRedisHttpSession
@EnableFeignClients(basePackages = "com.ctz.gulimail.cart.feign")
@EnableDiscoveryClient
@SpringBootApplication
public class GulimailCartApplication {

    public static void main(String[] args) {
        SpringApplication.run(GulimailCartApplication.class, args);
    }

}
