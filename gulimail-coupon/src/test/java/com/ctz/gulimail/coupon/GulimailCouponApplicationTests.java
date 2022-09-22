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
        int[] ints = {-1,2,3,4,5};
        System.out.println("t(ints) = " + t1(ints));
        System.out.println("t(ints) = " + t2(ints));
    }

    public int t1(int[] nums){
        int left = -1;
        int right = nums.length;
        while(left+1!=right){
            int mid = (left+right)/2;
            if(nums[mid]<=0){
                left = mid;
            } else {
                right = mid;
            }
        }
        return left;
    }

    public int t2(int[] nums){
        int left = -1;
        int right = nums.length;
        while(left+1!=right){
            int mid = (left+right)/2;
            if(nums[mid]<0){
                left = mid;
            } else {
                right = mid;
            }
        }
        return right;
    }

}
