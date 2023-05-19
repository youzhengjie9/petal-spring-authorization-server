package com.petal.oauth2.gateway;

import com.petal.oauth2.common.swagger.config.SwaggerConfig;
import com.petal.oauth2.common.swagger.config.WebMvcConfig;
import com.petal.oauth2.gateway.properties.GatewayConfigProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;

@SpringBootApplication
@EnableDiscoveryClient
@ComponentScan(value = "com.auth.server",excludeFilters = {@ComponentScan.Filter(
        type = FilterType.ASSIGNABLE_TYPE,classes = {SwaggerConfig.class, WebMvcConfig.class}
)})
@EnableConfigurationProperties(GatewayConfigProperties.class)
public class Oauth2GatewayApplication6677 {

    public static void main(String[] args) {
        SpringApplication.run(Oauth2GatewayApplication6677.class,args);
    }

}
