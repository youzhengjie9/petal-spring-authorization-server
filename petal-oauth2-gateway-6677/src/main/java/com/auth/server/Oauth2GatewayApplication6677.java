package com.auth.server;

import com.auth.server.config.Swagger3Config;
import com.auth.server.config.WebMvcConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;

@SpringBootApplication
@EnableDiscoveryClient
@ComponentScan(value = "com.auth.server",excludeFilters = {@ComponentScan.Filter(
        type = FilterType.ASSIGNABLE_TYPE,classes = {Swagger3Config.class, WebMvcConfig.class}
)})
public class Oauth2GatewayApplication6677 {

    public static void main(String[] args) {
        SpringApplication.run(Oauth2GatewayApplication6677.class,args);
    }

}
