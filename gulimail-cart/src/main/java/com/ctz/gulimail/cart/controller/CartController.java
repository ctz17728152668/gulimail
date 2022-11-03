package com.ctz.gulimail.cart.controller;

import com.ctz.common.to.UserInfoTo;
import com.ctz.gulimail.cart.interceptor.CartInterceptor;
import com.ctz.gulimail.cart.service.CartService;
import com.ctz.gulimail.cart.vo.Cart;
import com.ctz.gulimail.cart.vo.CartItem;
import lombok.extern.slf4j.Slf4j;
import org.bouncycastle.math.raw.Mod;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.concurrent.ExecutionException;

@Controller
@Slf4j
public class CartController {

    @Autowired
    private CartService cartService;


    /**
     * 跳转到购物车页面
     * 1.若没有用户则分配一个临时userkey 并请求结束后 接入cookie
     * 2.包含用户则直接获取cookie中的userkey
     * @return
     */
    @GetMapping("cart.html")
    public String Cart(Model model){
        //获取购物车
        Cart cart = cartService.getCart();
        model.addAttribute("cart",cart);
        return "cartList";
    }

    /**
     * 修改购物车商品是否选中
     * @param skuId
     * @param checked
     * @return
     */
    @GetMapping("checkItem")
    public String checkItem(@RequestParam("skuId") Long skuId,@RequestParam("checked") Integer checked){
        cartService.checkItem(skuId,checked);
        return "redirect:http://cart.gulimall.com/cart.html";
    }

    /**
     * 修改购物车商品数量
     * @param skuId
     * @param num
     * @return
     */
    @GetMapping("countItem")
    public String countItem(@RequestParam("skuId") Long skuId,@RequestParam("num") Integer num){
        cartService.countItem(skuId,num);
        return "redirect:http://cart.gulimall.com/cart.html";
    }

    /**
     * 删除购物车内指定商品
     * @param skuId
     * @return
     */
    @GetMapping("deleteItem")
    public String deleteItem(@RequestParam("skuId") Long skuId){
        cartService.deleteItem(skuId);
        return "redirect:http://cart.gulimall.com/cart.html";
    }

    /**
     * 向购物车中添加商品
     * @param skuId
     * @param num
     * @return
     */
    @GetMapping("/addCartItem")
    public String addToCart(@RequestParam("skuId") Long skuId,
                            @RequestParam("num") Integer num,
                            RedirectAttributes redirectAttributes) throws ExecutionException, InterruptedException {
        CartItem cartItem = cartService.addToCart(skuId,num);
        redirectAttributes.addAttribute("skuId",skuId);
        return "redirect:http://cart.gulimall.com/addToCartSuccess.html";
    }

    @GetMapping("addToCartSuccess.html")
    public String success(@RequestParam("skuId") Long skuId,Model model){
        CartItem cartItem = cartService.getCartItem(skuId);
        model.addAttribute("cartItem",cartItem);
        return "success";
    }



}
