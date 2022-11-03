package com.ctz.gulimail.cart.service;

import com.ctz.gulimail.cart.vo.Cart;
import com.ctz.gulimail.cart.vo.CartItem;

import java.util.concurrent.ExecutionException;

public interface CartService {

    CartItem addToCart(Long skuId, Integer num) throws ExecutionException, InterruptedException;

    CartItem getCartItem(Long skuId);

    Cart getCart();


    void clearCart(String userKey);

    void checkItem(Long skuId, Integer checked);

    void countItem(Long skuId, Integer num);

    void deleteItem(Long skuId);
}
