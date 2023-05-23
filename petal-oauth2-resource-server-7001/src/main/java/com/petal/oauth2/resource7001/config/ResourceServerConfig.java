package com.petal.oauth2.resource7001.config;

import com.petal.oauth2.resource7001.handler.DefaultAccessDeniedHandler;
import com.petal.oauth2.resource7001.handler.DefaultAuthenticationEntryPoint;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.oauth2.jwt.JwtClaimNames;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
import org.springframework.security.web.SecurityFilterChain;

/**
 * oauth2资源服务器配置
 * 作用: 当JwtDecoder解码器存在时生效
 *
 * @author youzhengjie
 * @date 2023/04/25 00:33:50
 */
@ConditionalOnBean(JwtDecoder.class)
@Configuration(proxyBeanMethods = false)
@EnableWebSecurity //启动SpringSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true) //开启SpringSecurity注解鉴权
public class ResourceServerConfig {

    /**
     * premit所有
     */
    private static final String[] PERMIT_ALL ={
            "/res/test0",
            "/res/test5"
    };

    /**
     * 资源服务器配置
     *
     * @param http the http
     * @return the security filter chain
     * @throws Exception the exception
     */
    @Bean
    SecurityFilterChain jwtSecurityFilterChain(HttpSecurity http) throws Exception {
        // 拒绝访问处理器 401
        DefaultAccessDeniedHandler accessDeniedHandler = new DefaultAccessDeniedHandler();
        // 认证失败处理器 403
        DefaultAuthenticationEntryPoint authenticationEntryPoint = new DefaultAuthenticationEntryPoint();

        return http
                // 设置不主动创建session
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .authorizeRequests()
                //这些请求允许所有人访问
                .antMatchers(PERMIT_ALL).permitAll()
                //除了PERMIT_ALL之外所有请求都要认证才能访问
                .anyRequest().authenticated()
                .and()
                // 异常处理
                .exceptionHandling(exceptionConfigurer -> exceptionConfigurer
                        // 拒绝访问
                        .accessDeniedHandler(accessDeniedHandler)
                        // 认证失败
                        .authenticationEntryPoint(authenticationEntryPoint)
                )
                // 说明该模块是资源服务器,和老版本的 @EnableResourceServer 效果一样，只不过 @EnableResourceServer 已经被弃用了
                .oauth2ResourceServer(resourceServer -> resourceServer
                        // 配置资源服务器的拒绝访问，认证失败处理器、JWT验证
                        .accessDeniedHandler(accessDeniedHandler)
                        .authenticationEntryPoint(authenticationEntryPoint)
                        .jwt()
                )
                .build();
    }


    /**
     * JWT转换器
     *
     * @return {@link JwtAuthenticationConverter}
     */
    @Bean
    JwtAuthenticationConverter jwtAuthenticationConverter() {
        JwtAuthenticationConverter jwtAuthenticationConverter = new JwtAuthenticationConverter();
        //OAuth2默认前缀是 SCOPE_ 、SpringSecurity默认前缀是ROLE_（由于这里是oauth2，所以默认前缀是 SCOPE_）
        JwtGrantedAuthoritiesConverter jwtGrantedAuthoritiesConverter = new JwtGrantedAuthoritiesConverter();
        jwtAuthenticationConverter.setJwtGrantedAuthoritiesConverter(jwtGrantedAuthoritiesConverter);
        //用户名可以放sub
        jwtAuthenticationConverter.setPrincipalClaimName(JwtClaimNames.SUB);
        return jwtAuthenticationConverter;
    }
}