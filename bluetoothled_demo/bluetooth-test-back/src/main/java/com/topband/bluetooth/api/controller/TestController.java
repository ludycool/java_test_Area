package com.topband.bluetooth.api.controller;

import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author ludi
 * @version 1.0
 * @date 2019/12/12 13:31
 * @remark
 */
@Slf4j
@Api(description = "测试 接口")
@RestController
@RequestMapping("/test")
public class TestController {

    @Value("${config.test.k1}")
    private String k1;

    @GetMapping("/getkey")
    public String getkey() {
        return k1;
    }
}
