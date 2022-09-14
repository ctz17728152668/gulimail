package com.ctz.gulimail.member;

import com.ctz.gulimail.member.entity.GrowthChangeHistoryEntity;
import com.ctz.gulimail.member.service.GrowthChangeHistoryService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
public class GulimailMemberApplicationTests {

    @Autowired
    private GrowthChangeHistoryService historyService;

    @Test
    public void contextLoads() {
        List<GrowthChangeHistoryEntity> list = historyService.list();

    }

}
