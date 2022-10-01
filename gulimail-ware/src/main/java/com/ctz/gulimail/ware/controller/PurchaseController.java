package com.ctz.gulimail.ware.controller;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import com.ctz.gulimail.ware.service.PurchaseDetailService;
import com.ctz.gulimail.ware.vo.MergeVo;
import com.ctz.gulimail.ware.vo.PurchaseDoneVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.ctz.gulimail.ware.entity.PurchaseEntity;
import com.ctz.gulimail.ware.service.PurchaseService;
import com.ctz.common.utils.PageUtils;
import com.ctz.common.utils.R;



/**
 * 采购信息
 *
 * @author ctz
 * @email 1660598843@qq.com
 * @date 2022-09-14 15:51:47
 */
@RestController
@RequestMapping("ware/purchase")
public class PurchaseController {
    @Autowired
    private PurchaseService purchaseService;

    @Autowired
    private PurchaseDetailService purchaseDetailService;

    /**
     * 列表
     */
    @RequestMapping("/list")
    //@RequiresPermissions("ware:purchase:list")
    public R list(@RequestParam Map<String, Object> params){
        PageUtils page = purchaseService.queryPage(params);

        return R.ok().put("page", page);
    }

    /**
     * 领取采购单
     * @param ids
     * @return
     */
    @PostMapping("received")
    public R received(@RequestBody List<Long> ids){
        purchaseService.received(ids);
        return R.ok();
    }

    /**
     * 完成采购
     * @param doneVo
     * @return
     */
    @PostMapping("done")
    public R done(@RequestBody PurchaseDoneVo doneVo){
        purchaseService.done(doneVo);
        return R.ok();
    }

    /**
     * 查询未领取的采购单
     * @return
     */
    @GetMapping("unreceive/list")
    public R unreceiveList(){
        PageUtils page = purchaseService.queryPageSimple();
        return R.ok().put("page",page);
    }

    /**
     * 合并采购需求
     * @param mergeVo
     * @return
     */
    @PostMapping("merge")
    public R mergePurchase(@RequestBody MergeVo mergeVo){
        purchaseDetailService.mergePurchase(mergeVo);
        return R.ok();
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{id}")
    //@RequiresPermissions("ware:purchase:info")
    public R info(@PathVariable("id") Long id){
		PurchaseEntity purchase = purchaseService.getById(id);

        return R.ok().put("purchase", purchase);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    //@RequiresPermissions("ware:purchase:save")
    public R save(@RequestBody PurchaseEntity purchase){
		purchaseService.save(purchase);

        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    //@RequiresPermissions("ware:purchase:update")
    public R update(@RequestBody PurchaseEntity purchase){
		purchaseService.updateById(purchase);

        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    //@RequiresPermissions("ware:purchase:delete")
    public R delete(@RequestBody Long[] ids){
		purchaseService.removeByIds(Arrays.asList(ids));

        return R.ok();
    }

}
