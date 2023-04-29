package com.auth.server.security;

import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.client.RestTemplate;

/**
 * 安全配置
 *
 * @author youzhengjie
 * @date 2023/04/26 11:01:44
 */
@Configuration(proxyBeanMethods = false)
public class SecurityConfiguration {

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
                // oauth2第三方登录
                .oauth2Login(Customizer.withDefaults())
                // oauth2客户端
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
