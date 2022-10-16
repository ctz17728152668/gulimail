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
                {9,12},{1,10},{4,11},{8,12},{3,9},{6,9},{6,7}
        };
        int minArrowShots = findMinArrowShots(ints);
        System.out.println("minArrowShots = " + minArrowShots);
    }

    public int findMinArrowShots(int[][] points) {
        Arrays.sort(points,(a,b)->Integer.compare(a[0],b[0]));

        int count = 1;
        for(int i=1;i<points.length;i++){
            if(points[i][0]>points[i-1][1])
                count++;
            else
                points[i][1] = Math.min(points[i][1],points[i-1][1]);
        }
        return count;
    }





}
