package com.auth.server.config;

import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.client.RestTemplate;

/**
 * 安全配置
 * spring oauth2 迁移指南：https://github.com/spring-projects/spring-security/wiki/OAuth-2.0-Migration-Guide
 *
 * @author youzhengjie
 * @date 2023/04/26 11:01:44
 */
@Configuration(proxyBeanMethods = false)
public class ClientSecurityConfig {

    /***
     * 安全配置
     * @param http http
     * @return SecurityFilterChain
     * @throws Exception exception
     */
    @Bean
    SecurityFilterChain oauth2SecurityFilterChain(HttpSecurity http) throws Exception {
        http.authorizeRequests(requests ->
                // 任何请求都需要认证
                requests.anyRequest().authenticated()
                )
                // 开启Oauth2的单点登录（Sso）,和老版本的 @EnableOAuth2Sso 效果一样，只不过 @EnableOAuth2Sso 已经被弃用了
                .oauth2Login(Customizer.withDefaults())
                // oauth2客户端，和老版本的 @EnableOAuth2Client 效果一样，只不过 @EnableOAuth2Client 已经被弃用了
                .oauth2Client()
                .and()
                // 退出登录
                .logout();
        return http.build();
    }

    /**
     * restTemplate
     *
     * @param restTemplateBuilder 其他模板编辑器
     * @return {@link RestTemplate}
     */
    @Bean
    public RestTemplate oauth2ClientRestTemplate(RestTemplateBuilder restTemplateBuilder) {
        return restTemplateBuilder.build();
    }

}
