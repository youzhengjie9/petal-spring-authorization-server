package com.petal.oauth2.auth;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * oauth2帐号密码或手机号登录认证
 * @author youzhengjie
 * @date 2023/05/04 18:22:57
 */
@SpringBootApplication
@EnableDiscoveryClient
public class Oauth2AuthApplication9000 {

    public static void main(String[] args) {
        SpringApplication.run(Oauth2AuthApplication9000.class,args);
    }

}
