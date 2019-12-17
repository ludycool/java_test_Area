package com.topband.bluetooth.api;


import com.topband.bluetooth.api.config.AppConfig;
import lombok.extern.slf4j.Slf4j;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

/* 内置tomcat*/

@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class}
        ,scanBasePackages = {"com.topband.bluetooth.common","com.topband.bluetooth.serviceImpl", "com.topband.bluetooth.api","com.topband.bluetooth.config"})
@MapperScan("com.topband.bluetooth.mapper")
@Slf4j
public class ApiApplication {

    public static void main(String[] args) {

        AppConfig.context=SpringApplication.run(ApiApplication.class, args);
    }


}
