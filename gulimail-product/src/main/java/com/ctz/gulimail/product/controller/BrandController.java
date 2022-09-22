package com.ctz.gulimail.product.controller;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.ctz.common.exception.BizCodeEnume;
import com.ctz.common.valid.AddGroup;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import com.ctz.gulimail.product.entity.BrandEntity;
import com.ctz.gulimail.product.service.BrandService;
import com.ctz.common.utils.PageUtils;
import com.ctz.common.utils.R;

import javax.validation.Valid;


/**
 * 品牌
 *
 * @author ctz
 * @email 1660598843@qq.com
 * @date 2022-09-12 16:51:16
 */
@RestController
@RequestMapping("product/brand")
public class BrandController {
    @Autowired
    private BrandService brandService;

    /**
     * 列表
     */
    @RequestMapping("/list")
    //@RequiresPermissions("product:brand:list")
    public R list(@RequestParam Map<String, Object> params){
        PageUtils page = brandService.queryPage(params);

        return R.ok().put("page", page);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{brandId}")
    //@RequiresPermissions("product:brand:info")
    public R info(@PathVariable("brandId") Long brandId){
		BrandEntity brand = brandService.getById(brandId);

        return R.ok().put("brand", brand);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    //@RequiresPermissions("product:brand:save")
    public R save(@Validated @RequestBody BrandEntity brand, BindingResult result){
        if (result.hasErrors()) {
            List<FieldError> fieldErrors = result.getFieldErrors();
            HashMap<String, String> map = new HashMap<>();
            for (FieldError error : fieldErrors) {
                String defaultMessage = error.getDefaultMessage();
                String field = error.getField();
                map.put(field,defaultMessage);
            }

            return R.error(BizCodeEnume.VALID_EXCEPTION.getCode(),"提交的数据不合法").put("data",map);
        } else {
            brandService.save(brand);
        }

        return R.ok();
    }

    /**
     * 修改
     */
    @PostMapping("/update")
    //@RequiresPermissions("product:brand:update")
    public R update(@RequestBody BrandEntity brand){
		brandService.updateById(brand);

        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    //@RequiresPermissions("product:brand:delete")
    public R delete(@RequestBody Long[] brandIds){
		brandService.removeByIds(Arrays.asList(brandIds));

        return R.ok();
    }

}
