package com.ctz.gulimail.product;

import com.alibaba.fastjson.JSON;
import com.ctz.gulimail.product.service.CategoryService;
import com.ctz.gulimail.product.vo.Catelog2Vo;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.redisson.Redisson;
import org.redisson.api.RLock;
import org.redisson.api.RSemaphore;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;
import java.util.Map;

@RunWith(SpringRunner.class)
@SpringBootTest
@Slf4j
public class GulimailProductApplicationTests {

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private RedissonClient redissonClient;


    @Test
    public void contextLoads() throws InterruptedException {
        RSemaphore lock = redissonClient.getSemaphore("lock");
        lock.acquire();
        Thread.sleep(10000);
        System.out.println(lock);
    }

}
