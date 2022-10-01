package com.ctz.gulimail.product.controller;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import com.ctz.gulimail.product.entity.ProductAttrValue;
import com.ctz.gulimail.product.service.ProductAttrValueService;
import com.ctz.gulimail.product.vo.AttrRespVo;
import com.ctz.gulimail.product.vo.AttrVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.ctz.gulimail.product.entity.AttrEntity;
import com.ctz.gulimail.product.service.AttrService;
import com.ctz.common.utils.PageUtils;
import com.ctz.common.utils.R;



/**
 * 商品属性
 *
 * @author ctz
 * @email 1660598843@qq.com
 * @date 2022-09-12 16:51:16
 */
@RestController
@RequestMapping("product/attr")
public class AttrController {
    @Autowired
    private AttrService attrService;

    @Autowired
    private ProductAttrValueService productAttrValueService;

    /**
     * 列表
     */
    @RequestMapping("/list")
    //@RequiresPermissions("product:attr:list")
    public R list(@RequestParam Map<String, Object> params){
        PageUtils page = attrService.queryPage(params);

        return R.ok().put("page", page);
    }

    @GetMapping("/{type}/list/{catelogId}")
    public R attrList(@RequestParam Map<String ,Object> params,
                      @PathVariable("catelogId") Long catelogId,
                      @PathVariable("type") String type){
        PageUtils page = attrService.attrList(params,catelogId,type);

        return R.ok().put("page", page);
    }


    /**
     * 获取spu规格
     * @param spuId
     * @return
     */
    @GetMapping("base/listforspu/{spuId}")
    public R getProductAttrBySpuId(@PathVariable Long spuId){
        List<ProductAttrValue> list = productAttrValueService.getValueBySpuId(spuId);
        return R.ok().put("data",list);
    }



    /**
     * 信息
     */
    @RequestMapping("/info/{attrId}")
    //@RequiresPermissions("product:attr:info")
    public R info(@PathVariable("attrId") Long attrId){
		AttrRespVo attr = attrService.getAttrById(attrId);

        return R.ok().put("attr", attr);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    //@RequiresPermissions("product:attr:save")
    public R save(@RequestBody AttrVo attr){
		attrService.saveAttr(attr);

        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    //@RequiresPermissions("product:attr:update")
    public R update(@RequestBody AttrVo attr){
		attrService.updateAttr(attr);
        return R.ok();
    }

    /**
     * 批量修改规格
     */
    @PostMapping("/update/{spuId}")
    //@RequiresPermissions("product:attr:update")
    public R  updateSpuAttr(@PathVariable("spuId") Long spuId,
                            @RequestBody List<ProductAttrValue> values){
        productAttrValueService.updateSpuAttr(spuId,values);
        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    //@RequiresPermissions("product:attr:delete")
    public R delete(@RequestBody Long[] attrIds){
		attrService.removeByIds(Arrays.asList(attrIds));

        return R.ok();
    }

}
