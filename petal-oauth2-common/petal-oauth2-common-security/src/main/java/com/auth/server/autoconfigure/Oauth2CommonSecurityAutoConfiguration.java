package com.auth.server.autoconfigure;

import com.auth.server.component.CustomAuthenticationEntryPoint;
import com.auth.server.component.PermitAllAspect;
import com.auth.server.component.SecurityMessageSourceConfiguration;
import com.auth.server.service.*;
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
    public CustomUserDetailsService passwordUserDetailsServiceImpl(){

        return new PasswordUserDetailsServiceImpl();
    }

    @Bean
    public CustomUserDetailsService smsUserDetailsServiceImpl(){

        return new SmsUserDetailsServiceImpl();
    }

    @Bean
    public RedisOAuth2AuthorizationService redisOAuth2AuthorizationService(){

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
