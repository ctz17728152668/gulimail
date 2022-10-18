package com.ctz.gulimail.product.config;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

//@EnableConfigurationProperties(MyThreadConfigProperties.class)
@Configuration
public class MyThreadConfig {

    @Bean
    public ThreadPoolExecutor threadPoolExecutor(MyThreadConfigProperties myThreadConfigProperties){

        return new ThreadPoolExecutor(myThreadConfigProperties.getCoreSize(),
                myThreadConfigProperties.getMaxSize(),
                myThreadConfigProperties.getKeepAliveTime(),
                TimeUnit.SECONDS,
                new LinkedBlockingQueue<>(100000),
                Executors.defaultThreadFactory(),new ThreadPoolExecutor.AbortPolicy());
    }
}
