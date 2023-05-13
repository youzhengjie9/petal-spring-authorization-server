package com.auth.server.autoconfigure;

import com.auth.server.service.CustomUserDetailsService;
import com.auth.server.service.CustomUserDetailsServiceImpl;
import com.auth.server.service.RedisOAuth2AuthorizationService;
import com.auth.server.service.CustomRemoteRegisteredClientRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClientRepository;

/**
 * 自动配置类
 *
 * @author youzhengjie
 * @date 2023/05/10 10:00:07
 */
@Configuration
public class Oauth2CommonSecurityAutoConfiguration {

    @Bean
    public RedisOAuth2AuthorizationService redisOAuth2AuthorizationService(){
        return new RedisOAuth2AuthorizationService();
    }

    @Bean
    public CustomUserDetailsService customUserDetailsService(){
        return new CustomUserDetailsServiceImpl();
    }

    @Bean
    public RegisteredClientRepository registeredClientRepository(){
        return new CustomRemoteRegisteredClientRepository();
    }

}
