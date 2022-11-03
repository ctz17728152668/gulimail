package com.ctz.gulimail.cart.interceptor;

import com.ctz.common.constant.AuthServerConstant;
import com.ctz.common.constant.CartConstant;
import com.ctz.common.to.UserInfoTo;
import com.ctz.common.vo.MemberRespVO;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.UUID;

public class CartInterceptor implements HandlerInterceptor {

    public static ThreadLocal<UserInfoTo> threadLocal = new ThreadLocal<>();

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        UserInfoTo userInfoTo = new UserInfoTo();
        HttpSession session = request.getSession();
        MemberRespVO member = (MemberRespVO)session.getAttribute(AuthServerConstant.LOGIN_USER_PREFIX);
        if(member!=null){
            userInfoTo.setUserId(member.getId());
        }
        Cookie[] cookies = request.getCookies();
        if(cookies!=null&&cookies.length>0){
            for (Cookie cookie : cookies) {
                if(cookie.getName().equals(CartConstant.TEMP_USER_COOKIE_KEY)){
                    userInfoTo.setUserKey(cookie.getValue());
                    userInfoTo.setHasUserKey(true);
                    break;
                }
            }
        }

        if(!userInfoTo.getHasUserKey()){
            //如果原本不含有userkey
            //则防止一个uuid作为userkey
            String uuid = UUID.randomUUID().toString();
            userInfoTo.setUserKey(uuid);
        }
        threadLocal.set(userInfoTo);
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        //如果之前不存在userkey 则需要添加userkey的cookie
        UserInfoTo userInfoTo = CartInterceptor.threadLocal.get();
        if(!userInfoTo.getHasUserKey()){
            Cookie cookie = new Cookie(CartConstant.TEMP_USER_COOKIE_KEY, userInfoTo.getUserKey());
            cookie.setDomain("gulimall.com");
            cookie.setMaxAge(CartConstant.TEMP_USER_COOKIE_TIMEOUT);
            response.addCookie(cookie);
        }
    }
}
