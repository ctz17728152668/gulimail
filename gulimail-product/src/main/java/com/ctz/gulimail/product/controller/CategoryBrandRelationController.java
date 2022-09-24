package com.ctz.gulimail.product.controller;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.ctz.gulimail.product.entity.CategoryBrandRelationEntity;
import com.ctz.gulimail.product.service.CategoryBrandRelationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.ctz.gulimail.product.entity.SpuInfoEntity;
import com.ctz.common.utils.PageUtils;
import com.ctz.common.utils.R;



/**
 *
 *
 * @author ctz
 * @email 1660598843@qq.com
 * @date 2022-09-12 16:51:16
 */
@RestController
@RequestMapping("product/categorybrandrelation")
public class CategoryBrandRelationController {
    @Autowired
    private CategoryBrandRelationService categoryBrandRelationService;

    /**
     * 产品分类和属性的关联信息列表
     */
    @RequestMapping("catelog/list")
    //@RequiresPermissions("product:spuinfo:list")
    public R list(@RequestParam("brandId") Long brandId){
        List<CategoryBrandRelationEntity> list = categoryBrandRelationService.list(
                new LambdaQueryWrapper<CategoryBrandRelationEntity>().
                        eq(CategoryBrandRelationEntity::getBrandId, brandId));
        return R.ok().put("data", list);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{id}")
    //@RequiresPermissions("product:spuinfo:info")
    public R info(@PathVariable("id") Long id){
        CategoryBrandRelationEntity data = categoryBrandRelationService.getById(id);

        return R.ok().put("data", data);
    }

    /**
     * 保存
     */
    @PostMapping("/save")
    //@RequiresPermissions("product:spuinfo:save")
    public R save(@RequestBody CategoryBrandRelationEntity categoryBrandRelationEntity){
        categoryBrandRelationService.saveDeatil(categoryBrandRelationEntity);

        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    //@RequiresPermissions("product:spuinfo:update")
    public R update(@RequestBody CategoryBrandRelationEntity categoryBrandRelationEntity){
        categoryBrandRelationService.updateById(categoryBrandRelationEntity);

        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    //@RequiresPermissions("product:spuinfo:delete")
    public R delete(@RequestBody Long[] ids){
        categoryBrandRelationService.removeByIds(Arrays.asList(ids));

        return R.ok();
    }

}
