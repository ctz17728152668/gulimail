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
        List<List<String>> list = new ArrayList<>();
        ArrayList<String> a1 = new ArrayList<>();
        a1.add("JFK");
        a1.add("KUL");
        list.add(a1);
        ArrayList<String> a2 = new ArrayList<>();
        a2.add("JFK");
        a2.add("NRT");
        list.add(a2);
        ArrayList<String> a3 = new ArrayList<>();
        a3.add("NRT");
        a3.add("JFK");
        list.add(a3);
        /*ArrayList<String> a4 = new ArrayList<>();
        a4.add("ATL");
        a4.add("JFK");
        list.add(a4);
        ArrayList<String> a5 = new ArrayList<>();
        a5.add("ATL");
        a5.add("SFO");
        list.add(a5);*/
        findItinerary(list);
        System.out.println("res = " + res);
    }

    Map<String, List<String>> map = new HashMap<>();
    List<String> res = new ArrayList<>();
    boolean flag = false;
    int size = 0;
    public List<String> findItinerary(List<List<String>> tickets) {
        map.clear();
        res.clear();
        flag = false;
        size = 0;
        for(List<String> list : tickets){
            String key = list.get(0);
            if(!map.containsKey(key)){
                map.put(key,new ArrayList<>());
            }
            map.get(key).add(list.get(1));
            size++;
        }
        for (List<String> value : map.values()) {
            Collections.sort(value);
        }
        res.add("JFK");
        a("JFK");
        return res;
    }

    public void a(String start){
        if(res.size()==size+1){
            flag = true;
            return;
        }

        List<String> list = map.get(start);
        if(list==null)return;
        int length = list.size();
        for(int i=0;i<length;i++){
            String s = list.remove(i);
            res.add(s);
            a(s);
            if(flag)return;
            res.remove(res.size()-1);
            list.add(i,s);
        }
    }
}
