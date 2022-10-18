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
                {-52,31},{-73,-26},{82,97},{-65,-11},{-62,-49},{95,99},{58,95},{-31,49},{66,98}
                ,{-63,2},{30,47},{-40,-26}
        };
        int i = eraseOverlapIntervals(ints);
    }

    public int eraseOverlapIntervals(int[][] intervals) {
        Arrays.sort(intervals,(a,b)-> a[1]-b[1]);
        int count = 0;
        for(int i=1;i<intervals.length;i++){
            if(intervals[i-1][1]>intervals[i][0]){
                count++;
                intervals[i][1]=intervals[i-1][1];
            }
        }
        System.out.println(count);
        return count;
    }





}
