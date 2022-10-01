package com.ctz.gulimail.ware.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.ctz.common.constant.WareConstant;
import com.ctz.gulimail.ware.entity.PurchaseDetailEntity;
import com.ctz.gulimail.ware.entity.WareSkuEntity;
import com.ctz.gulimail.ware.service.PurchaseDetailService;
import com.ctz.gulimail.ware.service.WareSkuService;
import com.ctz.gulimail.ware.vo.ItemVo;
import com.ctz.gulimail.ware.vo.PurchaseDoneVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ctz.common.utils.PageUtils;
import com.ctz.common.utils.Query;

import com.ctz.gulimail.ware.dao.PurchaseDao;
import com.ctz.gulimail.ware.entity.PurchaseEntity;
import com.ctz.gulimail.ware.service.PurchaseService;
import org.springframework.transaction.annotation.Transactional;


@Service("purchaseService")
public class PurchaseServiceImpl extends ServiceImpl<PurchaseDao, PurchaseEntity> implements PurchaseService {


    @Autowired
    private PurchaseService purchaseService;

    @Autowired
    private PurchaseDetailService purchaseDetailService;

    @Autowired
    private WareSkuService wareSkuService;

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<PurchaseEntity> page = this.page(
                new Query<PurchaseEntity>().getPage(params),
                new QueryWrapper<PurchaseEntity>()
        );

        return new PageUtils(page);
    }

    @Override
    public PageUtils queryPageSimple() {
        IPage<PurchaseEntity> page = this.page(
                new Query<PurchaseEntity>().getPage(new HashMap()),
                new LambdaQueryWrapper<PurchaseEntity>().eq(PurchaseEntity::getStatus,0).or().eq(PurchaseEntity::getStatus,1)
        );

        return new PageUtils(page);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void received(List<Long> ids) {
        //1.只能接收 新建或已分配状态的采购单
        List<PurchaseEntity> collect = purchaseService.listByIds(ids).stream().filter((item) -> item.getStatus() == WareConstant.PurchaseStatusEnum.CREATED.getCode() ||
                item.getStatus() == WareConstant.PurchaseStatusEnum.ASSIGNED.getCode())
                .map((item)->{
                    item.setStatus(WareConstant.PurchaseStatusEnum.RECEIVE.getCode());
                    return item;
                })
                .collect(Collectors.toList());
        //2.改变采购单的状态
        purchaseService.updateBatchById(collect);

        //3.改变采购需求的状态
        collect.forEach((item)->{
            List<PurchaseDetailEntity> collect1 = purchaseDetailService.list(new LambdaQueryWrapper<PurchaseDetailEntity>()
                    .eq(PurchaseDetailEntity::getPurchaseId, item.getId())).stream()
                    .map((detail) -> {
                        PurchaseDetailEntity purchaseDetailEntity = new PurchaseDetailEntity();
                        purchaseDetailEntity.setId(detail.getId());
                        purchaseDetailEntity.setStatus(WareConstant.PurchaseDetailStatusEnum.BUYING.getCode());
                        return purchaseDetailEntity;
                    }).collect(Collectors.toList());
            purchaseDetailService.updateBatchById(collect1);
        });
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void done(PurchaseDoneVo doneVo) {
        //1.改变所有采购详情 和采购单的状态
        Long id = doneVo.getId();
        List<ItemVo> items = doneVo.getItems();
        ArrayList<PurchaseDetailEntity> entities = new ArrayList<>();
        ArrayList<WareSkuEntity> wareSkuEntities = new ArrayList<>();
        boolean flag = true;
        for (ItemVo item : items) {
            PurchaseDetailEntity purchaseDetailEntity = new PurchaseDetailEntity();
            purchaseDetailEntity.setId(item.getItemId());
            //如果该需求失败
            if(item.getStatus() == WareConstant.PurchaseDetailStatusEnum.HASERROR.getCode()){
                purchaseDetailEntity.setStatus(item.getStatus());
                flag = false;
            } else {
                purchaseDetailEntity.setStatus(WareConstant.PurchaseDetailStatusEnum.FINISH.getCode());
                //需求成功 需要入库
                wareSkuService.saveOrUpdateSku(item.getItemId());
            }
            entities.add(purchaseDetailEntity);
        }
        //更新采购需求状态
        purchaseDetailService.updateBatchById(entities);
        //更新采购单状态
        PurchaseEntity purchaseEntity = new PurchaseEntity();
        purchaseEntity.setId(id);
        purchaseEntity.setStatus(flag?WareConstant.PurchaseStatusEnum.FINISH.getCode():WareConstant.PurchaseStatusEnum.HASERROR.getCode());
        purchaseService.updateById(purchaseEntity);

        //更新库存

    }

}