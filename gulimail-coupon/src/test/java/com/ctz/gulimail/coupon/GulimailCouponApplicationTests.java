package com.ctz.gulimail.coupon;

import com.ctz.gulimail.coupon.entity.CouponEntity;
import com.ctz.gulimail.coupon.service.CouponService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
public class GulimailCouponApplicationTests {

    @Resource
    private CouponService couponService;

    @Test
    public void contextLoads() {
        CouponEntity couponEntity = new CouponEntity();
        couponEntity.setCouponName("abc");
        couponService.save(couponEntity);
    }

}
