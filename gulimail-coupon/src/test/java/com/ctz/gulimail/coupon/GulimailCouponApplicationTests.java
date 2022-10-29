package com.ctz.gulimail.coupon;

import com.ctz.gulimail.coupon.entity.CouponEntity;
import com.ctz.gulimail.coupon.service.CouponService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
import java.lang.reflect.Array;
import java.util.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class GulimailCouponApplicationTests {


    @Test
    public void contextLoads() {
        int[][] ints = {
                {1,3},{2,6},{8,10},{15,18}
        };
        merge(ints);
    }

    public int[][] merge(int[][] intervals) {
        Arrays.sort(intervals,(a,b)->a[0]-b[0]);
        return null;
    }





}
