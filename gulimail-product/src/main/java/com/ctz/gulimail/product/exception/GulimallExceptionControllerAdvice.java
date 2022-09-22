package com.ctz.gulimail.product.exception;

import com.ctz.common.utils.R;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.xml.bind.ValidationException;
import java.util.HashMap;

@Slf4j
@RestControllerAdvice(basePackages = "com.ctz.gulimail")
public class GulimallExceptionControllerAdvice {

    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    public R handleVaildException(MethodArgumentNotValidException e){
        log.debug("数据校验出现问题{},异常类型为{}",e.getMessage(),e.getClass());
        BindingResult bindingResult = e.getBindingResult();
        HashMap<String, String> map = new HashMap<>();
        bindingResult.getFieldErrors().forEach((fieldError -> {
            map.put(fieldError.getField(),fieldError.getDefaultMessage());
        }));
        return R.error(400,"参数校验错误").put("data",map);
    }
}
