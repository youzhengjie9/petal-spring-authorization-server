package com.auth.server;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class Oauth2ResourceServerApplication7000 {

    public static void main(String[] args) {

        SpringApplication.run(Oauth2ResourceServerApplication7000.class,args);

    }

}
