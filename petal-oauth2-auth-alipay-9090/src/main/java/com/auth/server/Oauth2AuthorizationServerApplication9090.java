package com.auth.server;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Petal的第三方认证系统（和Gitee、微信等第三方登录类似，只不过这个第三方登录是我们自己实现的）
 * 采用：授权码模式
 * @author youzhengjie
 * @date 2023/04/20 18:01:21
 */
@SpringBootApplication
public class Oauth2AuthorizationServerApplication9090 {

    public static void main(String[] args) {
        SpringApplication.run(Oauth2AuthorizationServerApplication9090.class,args);
    }

}
