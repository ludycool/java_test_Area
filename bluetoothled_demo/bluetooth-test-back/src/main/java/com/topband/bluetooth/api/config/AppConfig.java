package com.topband.bluetooth.api.config;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Configuration;

/**
 * 此类 可以静态类中 获取配置数据， 在配置中心中更改，可以动态同步
 * @author ludi
 * @version 1.0
 * @date 2019/10/31 17:35
 * @remark
 */
@Configuration
public class AppConfig {

    public static ConfigurableApplicationContext context;
    /**
     * 根据key 获取配置
     *
     * @param key
     * @return
     */
    public static String getValueBykey(String key) {
        if (context != null) {
            return context.getEnvironment().getProperty(key, "");
        } else {
            return"";
        }
    }
}
