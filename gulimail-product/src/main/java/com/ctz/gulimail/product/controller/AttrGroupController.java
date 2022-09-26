package com.ctz.gulimail.product.controller;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import com.ctz.gulimail.product.entity.AttrAttrgroupRelationEntity;
import com.ctz.gulimail.product.entity.AttrEntity;
import com.ctz.gulimail.product.service.AttrAttrgroupRelationService;
import com.ctz.gulimail.product.service.AttrService;
import com.ctz.gulimail.product.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.ctz.gulimail.product.entity.AttrGroupEntity;
import com.ctz.gulimail.product.service.AttrGroupService;
import com.ctz.common.utils.PageUtils;
import com.ctz.common.utils.R;



/**
 * 属性分组
 *
 * @author ctz
 * @email 1660598843@qq.com
 * @date 2022-09-12 16:51:16
 */
@RestController
@RequestMapping("product/attrgroup")
public class AttrGroupController {
    @Autowired
    private AttrGroupService attrGroupService;

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private AttrAttrgroupRelationService relationService;


    @Autowired
    private AttrService attrService;

    /**
     * 列表
     */
    @RequestMapping("list/{catelogId}")
    //@RequiresPermissions("product:attrgroup:list")
    public R list(@RequestParam Map<String, Object> params,@PathVariable("catelogId") Long catelogId){
//        PageUtils page = attrGroupService.queryPage(params);
        PageUtils page = attrGroupService.queryPageByCatelogId(params,catelogId);

        return R.ok().put("page", page);
    }


    /**
     * 删除属性与分组的关联关系
     */
    @PostMapping("attr/relation/delete")
    public R deleteRelation(@RequestBody AttrAttrgroupRelationEntity[] relations){
        relationService.deleteRelations(relations);
        return R.ok();
    }

    /**
     * 获取属性分组没有关联的其他属性
     */
    @GetMapping("/{attrgroupId}/noattr/relation")
    public R getNoattrByGroupId(@RequestParam Map<String, Object> params,
                                @PathVariable("attrgroupId") Long attrgroupId){

        PageUtils page = attrService.getNoattrByGroupId(params,attrgroupId);
        return R.ok().put("page",page);
    }

    /**
     * 获取属性分组的关联的所有属性
     */
    @GetMapping("/{attrgroupId}/attr/relation")
    public R getAttrByGroupId(@PathVariable("attrgroupId") Long attrgroupId){
        List<AttrEntity> list = attrGroupService.getAttrByGroupId(attrgroupId);
        return R.ok().put("data",list);
    }


    /**
     * 添加属性与分组的关联关系
     */
    @PostMapping("attr/relation")
    public R addRelation(@RequestBody AttrAttrgroupRelationEntity[] relationEntities){
        relationService.saveBatch(Arrays.asList(relationEntities));
        return R.ok();
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{attrGroupId}")
    //@RequiresPermissions("product:attrgroup:info")
    public R info(@PathVariable("attrGroupId") Long attrGroupId){
		AttrGroupEntity attrGroup = attrGroupService.getById(attrGroupId);

        attrGroup.setCatelogPath(categoryService.getPath(attrGroup.getCatelogId()));
        return R.ok().put("attrGroup", attrGroup);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    //@RequiresPermissions("product:attrgroup:save")
    public R save(@RequestBody AttrGroupEntity attrGroup){
		attrGroupService.save(attrGroup);

        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    //@RequiresPermissions("product:attrgroup:update")
    public R update(@RequestBody AttrGroupEntity attrGroup){
		attrGroupService.updateById(attrGroup);

        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    //@RequiresPermissions("product:attrgroup:delete")
    public R delete(@RequestBody Long[] attrGroupIds){
		attrGroupService.removeByIds(Arrays.asList(attrGroupIds));

        return R.ok();
    }

}
