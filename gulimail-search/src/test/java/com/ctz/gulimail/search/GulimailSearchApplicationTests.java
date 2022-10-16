package com.ctz.gulimail.search;

import com.alibaba.fastjson.JSON;
import com.ctz.gulimail.search.config.ElasticSearchConfig;
import lombok.Data;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;

import static com.ctz.gulimail.search.config.ElasticSearchConfig.COMMON_OPTIONS;

@RunWith(SpringRunner.class)
@SpringBootTest
public class GulimailSearchApplicationTests {

    @Autowired
    private RestHighLevelClient client;

    @Test
    public void contextLoads() throws IOException {
        String test = "16_HUAWEI+Kirin+980";
        test.replace("+", "%20");
        System.out.println(test);
    }

    @Data
    class User{
        private String userName;
        private String gender;
        private Integer age;
    }

}
