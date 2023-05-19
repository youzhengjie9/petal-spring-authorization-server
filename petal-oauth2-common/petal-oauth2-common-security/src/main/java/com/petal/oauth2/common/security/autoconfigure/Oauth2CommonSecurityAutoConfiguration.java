package com.petal.oauth2.common.security.autoconfigure;

import com.petal.oauth2.common.security.component.PermitAllAspect;
import com.petal.oauth2.common.security.component.SecurityMessageSourceConfiguration;
import com.auth.server.service.*;
import com.petal.oauth2.alipay.auth.service.*;
import com.petal.oauth2.auth.service.*;
import com.petal.oauth2.common.security.service.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationService;
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
    public CustomUserDetailsService passwordUserDetailsServiceImpl(){

        return new PasswordUserDetailsServiceImpl();
    }

    @Bean
    public CustomUserDetailsService smsUserDetailsServiceImpl(){

        return new SmsUserDetailsServiceImpl();
    }

    @Bean
    public OAuth2AuthorizationService redisOAuth2AuthorizationService(){

        return new RedisOAuth2AuthorizationService();
    }

    @Bean
    public RedisOAuth2AuthorizationConsentService redisOAuth2AuthorizationConsentService(){

        return new RedisOAuth2AuthorizationConsentService();
    }

    @Bean
    public PermitAllAspect permitAllAspect(){

        return new PermitAllAspect();
    }

    @Bean
    public SecurityMessageSourceConfiguration securityMessageSourceConfiguration(){

        return new SecurityMessageSourceConfiguration();
    }

    @Bean
    public RegisteredClientRepository registeredClientRepository(){

        return new CustomRemoteRegisteredClientRepository();
    }

}
