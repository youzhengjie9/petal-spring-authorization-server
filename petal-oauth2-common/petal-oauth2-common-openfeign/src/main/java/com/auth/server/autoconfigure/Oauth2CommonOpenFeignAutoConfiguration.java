package com.auth.server.autoconfigure;

import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Configuration;

/**
 * feign自动配置类
 *
 * @author youzhengjie
 * @date 2023/05/10 11:15:05
 */
@Configuration
@EnableFeignClients(basePackages = {
        "com.auth.server.feign"
})
public class Oauth2CommonOpenFeignAutoConfiguration {


}
