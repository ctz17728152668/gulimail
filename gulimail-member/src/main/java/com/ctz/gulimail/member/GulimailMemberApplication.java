package com.ctz.gulimail.member;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@MapperScan("com.ctz.gulimail.member.dao")
@SpringBootApplication
public class GulimailMemberApplication {

    public static void main(String[] args) {
        SpringApplication.run(GulimailMemberApplication.class, args);
    }

}
