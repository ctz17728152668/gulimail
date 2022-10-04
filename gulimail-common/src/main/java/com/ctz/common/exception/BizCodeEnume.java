package com.ctz.common.exception;

/**
 * 通用枚举类
 */
public enum BizCodeEnume {

    UNKNOW_EXCEPTION(10000,"系统位置异常"),
    VALID_EXCEPTION(10001,"参数格式校验失败"),
    PRODUCT_UP_EXCEPTION(11000,"商品上架失败");


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
