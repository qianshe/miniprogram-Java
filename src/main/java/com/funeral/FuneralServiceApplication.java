package com.funeral;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.funeral.mapper")
public class FuneralServiceApplication {
    public static void main(String[] args) {
        SpringApplication.run(FuneralServiceApplication.class, args);
    }
} 