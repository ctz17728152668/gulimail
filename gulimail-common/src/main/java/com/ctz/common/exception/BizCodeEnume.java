package com.ctz.common.exception;

/**
 * 通用枚举类
 * 10:通用
 *      001:参数格式校验
 *      002:短信验证码频率
 * 11:商品服务
 * 12:订单服务
 * 13:购物车服务
 * 14:物流服务
 * 15:用户服务
 */
public enum BizCodeEnume {

    UNKNOW_EXCEPTION(10000,"系统位置异常"),
    VALID_EXCEPTION(10001,"参数格式校验失败"),
    SMS_CODE_EXCEPTION(10002,"短信验证码频率太高"),
    PRODUCT_UP_EXCEPTION(11000,"商品上架失败"),
    USERNAME_EXIST_EXCEPTION(15001,"用户名已存在"),
    PHONE_EXIST_EXCEPTION(15002,"手机号已存在"),
    USERNAME_PASSWORD_INVALID_EXCEPTION(15003,"账号密码错误");


    private String msg;

    private Integer code;

    BizCodeEnume(Integer code,String msg){
        this.code = code;
        this.msg = msg;
    }

    public Integer getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }
}
