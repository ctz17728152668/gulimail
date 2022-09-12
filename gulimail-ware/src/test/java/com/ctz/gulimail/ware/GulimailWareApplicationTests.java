package com.ctz.gulimail.ware;


import org.junit.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.Deque;
import java.util.LinkedList;
import java.util.List;

@SpringBootTest
public class GulimailWareApplicationTests {

    @Test
    public void contextLoads() {
        List<List<Integer>> permute = permute(new int[]{1, 2, 3});
        permute.forEach(System.out::println);
    }

    List<List<Integer>> res = new ArrayList<>();
    Deque<Integer> d = new LinkedList<>();
    int length;
    public List<List<Integer>> permute(int[] nums) {
        res.clear();
        length = nums.length;
        a(nums,0);
        return res;
    }

    public void a(int[] nums,int start){
        if(start==length){
            res.add(new ArrayList(d));
            return;
        }
        for(int i=start;i<length;i++){
            d.offerLast(nums[i]);
            swap(nums,start,i);
            a(nums,start+1);
            d.pollLast();
            swap(nums,start,i);
        }
    }

    public void swap(int[] nums,int left,int right){
        nums[left]^=nums[right];
        nums[right]^=nums[left];
        nums[left]^=nums[right];
    }

}
