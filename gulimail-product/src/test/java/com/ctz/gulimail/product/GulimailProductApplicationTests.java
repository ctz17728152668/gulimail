package com.ctz.gulimail.product;

import com.ctz.gulimail.product.entity.BrandEntity;
import com.ctz.gulimail.product.service.BrandService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class GulimailProductApplicationTests {

    @Autowired
    private BrandService brandService;

    @Test
    public void contextLoads() {
        System.out.println(brandService == null);
        BrandEntity brandEntity = new BrandEntity();
        brandEntity.setName("aa");
        brandService.save(brandEntity);

    }

}
