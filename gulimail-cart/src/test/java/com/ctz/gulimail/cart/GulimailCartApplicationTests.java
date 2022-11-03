package com.ctz.gulimail.cart;

import com.ctz.gulimail.cart.vo.Cart;
import com.ctz.gulimail.cart.vo.CartItem;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RunWith(SpringRunner.class)
@SpringBootTest
public class GulimailCartApplicationTests {

    @Test
    public void contextLoads() {
        ArrayList<CartItem> list = new ArrayList<>();
        CartItem cartItem = new CartItem();
        cartItem.setSkuId(1L);
        CartItem cartItem1 = new CartItem();
        cartItem.setSkuId(2L);
        CartItem cartItem2 = new CartItem();
        cartItem.setSkuId(1L);
        list.add(cartItem);
        list.add(cartItem1);
        list.add(cartItem2);
        Map<Long, List<CartItem>> collect = list.stream().collect(Collectors.groupingBy(CartItem::getSkuId));


    }

}
