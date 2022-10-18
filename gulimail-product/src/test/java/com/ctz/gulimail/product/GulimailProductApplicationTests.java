package com.ctz.gulimail.product;

import com.alibaba.fastjson.JSON;
import com.ctz.gulimail.product.service.AttrGroupService;
import com.ctz.gulimail.product.service.CategoryService;
import com.ctz.gulimail.product.vo.Catelog2Vo;
import com.ctz.gulimail.product.vo.SkuItemVo;
import lombok.AllArgsConstructor;
import lombok.Data;
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
import java.util.Optional;

@RunWith(SpringRunner.class)
@SpringBootTest
@Slf4j
public class GulimailProductApplicationTests {

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private RedissonClient redissonClient;

    @Autowired
    private AttrGroupService attrGroupService;

    @Test
    public void contextLoads() throws InterruptedException {
        A aa = new A("AA", null);
        B bb = new B("BB", null);
        C cc = new C("CC");

        aa.setB(bb);
        bb.setC(cc);

        Optional<A> aa1 = Optional.ofNullable(aa);
        C c = aa1.map(A::getB)
                .map(B::getC)
                .orElse(null);
        System.out.println(c);
    }

    @Data
    @AllArgsConstructor
    class A {
        private String name;
        private B b;
    }

    @Data
    @AllArgsConstructor
    class B {
        private String name;
        private C c;
    }

    @Data
    @AllArgsConstructor
    class C {
        private String name;
    }

}
