package com.petal.oauth2.resource7000;

import com.petal.oauth2.common.security.annotation.EnablePetalOauth2ResourceServer;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
@EnablePetalOauth2ResourceServer
public class Oauth2ResourceServerApplication7000 {

    public static void main(String[] args) {

        SpringApplication.run(Oauth2ResourceServerApplication7000.class,args);

    }

}
