package com.ctz.gulimail.cart.service.impl;

import com.alibaba.fastjson.JSON;
import com.ctz.common.constant.CartConstant;
import com.ctz.common.to.SkuInfoTO;
import com.ctz.common.to.UserInfoTo;
import com.ctz.common.utils.R;
import com.ctz.gulimail.cart.feign.ProductFeignService;
import com.ctz.gulimail.cart.interceptor.CartInterceptor;
import com.ctz.gulimail.cart.service.CartService;
import com.ctz.gulimail.cart.vo.Cart;
import com.ctz.gulimail.cart.vo.CartItem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.BoundHashOperations;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import rx.Completable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.stream.Collectors;

@Service
public class CartServiceImpl implements CartService {

    @Autowired
    private ProductFeignService productFeignService;


    @Autowired
    private StringRedisTemplate redisTemplate;

    @Autowired
    private ThreadPoolExecutor executor;

    @Override
    public CartItem addToCart(Long skuId, Integer num) throws ExecutionException, InterruptedException {
        //绑定操作 该操作绑定该key 的hash
        BoundHashOperations<String, Object, Object> boundHashOps = getBoundHashOps();

        String s = (String) boundHashOps.get(skuId.toString());
        CartItem cartItem = JSON.parseObject(s, CartItem.class);
        if (cartItem != null) {
            //如果购物车内含有此商品 只需要添加数量
            cartItem.setCount(cartItem.getCount() + num);

        } else {
            cartItem = new CartItem();
            //1.封装基本属性
            CartItem finalCartItem = cartItem;
            CompletableFuture<Void> skuInfoTask = CompletableFuture.runAsync(() -> {
                R r = productFeignService.getSkuInfo(skuId);
                String jsonString = JSON.toJSONString(r.get("skuInfo"));
                SkuInfoTO skuInfoTO = JSON.parseObject(jsonString, SkuInfoTO.class);
                finalCartItem.setCount(num);
                finalCartItem.setImage(skuInfoTO.getSkuDefaultImg());
                finalCartItem.setPrice(skuInfoTO.getPrice());
                finalCartItem.setSkuId(skuId);
                finalCartItem.setTitle(skuInfoTO.getSkuTitle());
            }, executor);


            //2.封装sku所属套餐信息
            CompletableFuture<Void> listTask = CompletableFuture.runAsync(() -> {
                List<String> stringList = productFeignService.getStringList(skuId);
                finalCartItem.setSkuAttrValues(stringList);
            }, executor);
            CompletableFuture.allOf(skuInfoTask, listTask).get();
        }

        String jsonString = JSON.toJSONString(cartItem);
        boundHashOps.put(skuId.toString(), jsonString);

        return cartItem;
    }

    @Override
    public CartItem getCartItem(Long skuId) {
        BoundHashOperations<String, Object, Object> boundHashOps = getBoundHashOps();
        String json = boundHashOps.get(skuId.toString()).toString();
        CartItem cartItem = JSON.parseObject(json, CartItem.class);
        return cartItem;
    }

    /**
     * 获取当前访问用户的购物车情况
     * @return
     */
    @Override
    public Cart getCart() {
        Cart cart = new Cart();
        UserInfoTo userInfoTo = CartInterceptor.threadLocal.get();
        Long userId = userInfoTo.getUserId();
        String userKey = userInfoTo.getUserKey();
        List<CartItem> mergeItems = null;
        //如果当前存在用户登录 则需要返回当前用户购物车与临时购物车的合并
        if (userId != null) {
            mergeItems = getMergeItems(userId,userKey);
            //更新当前用户购物车信息
            Map<String, String > collect = mergeItems.stream().collect(Collectors.toMap(obj -> obj.getSkuId().toString(), obj -> JSON.toJSONString(obj)));
            redisTemplate.opsForHash().putAll(CartConstant.CART_PREFIX_KEY+userId,collect);
            //并且删除临时购物车
            clearCart(userKey);
        } else {
            //如果当前没有用户登录 则直接返回临时购物车
            mergeItems = getCartItemsByUserKey(userKey);
        }
        cart.setItems(mergeItems);
        return cart;
    }

    /**
     * 删除指定购物车
     * @param userKey
     */
    @Override
    public void clearCart(String userKey) {
        redisTemplate.delete(CartConstant.CART_PREFIX_KEY+ userKey);
    }

    @Override
    public void checkItem(Long skuId, Integer checked) {
        BoundHashOperations<String, Object, Object> boundHashOps = getBoundHashOps();
        CartItem cartItem = getCartItem(skuId);
        cartItem.setCheck(checked==1);
        boundHashOps.put(cartItem.getSkuId().toString(),JSON.toJSONString(cartItem));
    }

    @Override
    public void countItem(Long skuId, Integer num) {
        BoundHashOperations<String, Object, Object> boundHashOps = getBoundHashOps();
        CartItem cartItem = getCartItem(skuId);
        cartItem.setCount(num);
        boundHashOps.put(cartItem.getSkuId().toString(),JSON.toJSONString(cartItem));
    }

    @Override
    public void deleteItem(Long skuId) {
        BoundHashOperations<String, Object, Object> boundHashOps = getBoundHashOps();
        boundHashOps.delete(skuId.toString());
    }


    /**
     * 获取用户 当前购物车和临时购物车合并后的商品列表
     * @return
     */
    private List<CartItem> getMergeItems(Long userId,String userKey){
        ArrayList<CartItem> mergeItems = new ArrayList<>();

        //如果当前有用户登录 则需要将 临时购物车和当前用户购物车合并后返回
        List<CartItem> items = getCartItemsByUserKey(userId.toString());
        List<CartItem> tempItems = getCartItemsByUserKey(userKey);

        ArrayList<CartItem> allItems = new ArrayList<>();

        if(!CollectionUtils.isEmpty(items)){
            allItems.addAll(items);
        }
        if(!CollectionUtils.isEmpty(tempItems)){
            allItems.addAll(tempItems);
        }

        Map<Long, List<CartItem>> collect = allItems.stream().collect(Collectors.groupingBy(CartItem::getSkuId));
        for (List<CartItem> value : collect.values()) {
            Integer sum = value.stream().map(CartItem::getCount).reduce(Integer::sum).orElse(0);
            CartItem cartItem = value.get(0);
            cartItem.setCount(sum);
            mergeItems.add(cartItem);
        }
        return mergeItems;
    }

    /**
     * 根据key值返回购物车内所有购物项
     * @param key
     * @return
     */
    private List<CartItem> getCartItemsByUserKey(String key) {
        String str = CartConstant.CART_PREFIX_KEY + key;
        List<Object> values = redisTemplate.opsForHash().values(str);
        if (!CollectionUtils.isEmpty(values)) {
            List<CartItem> collect = values.stream()
                    .map((obj) -> {
                        return JSON.parseObject(obj.toString(), CartItem.class);
                    }).collect(Collectors.toList());
            return collect;
        }

        return null;
    }

    private BoundHashOperations<String, Object, Object> getBoundHashOps() {
        UserInfoTo userInfoTo = CartInterceptor.threadLocal.get();
        String key = "";
        //如果不是临时用户 则key为用户id 否则为临时userkey
        key = CartConstant.CART_PREFIX_KEY + (StringUtils.isEmpty(userInfoTo.getUserId()) ? userInfoTo.getUserKey() : userInfoTo.getUserId());
        BoundHashOperations<String, Object, Object> operations = redisTemplate.boundHashOps(key);
        return operations;
    }


}
