package com.auth.server.config;

import com.auth.server.handler.DefaultAccessDeniedHandler;
import com.auth.server.handler.DefaultAuthenticationEntryPoint;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
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
public class OAuth2ResourceServerConfiguration {

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
                // security的session生成策略改为security不主动创建session即STALELESS
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                // "SCOPE_"是授权范围的前缀。（oauth2前缀有三个（SCOPE、ROLE、AUTH），从左到右粒度越来越细，oauth2默认前缀是SCOPE_）
                // 下面的意思是拥有“read或者all”授权范围任意一个就可以访问“/res/test1”
                // 拥有“write666或者all”授权范围任意一个就可以访问“/res/test2”
                .authorizeRequests()
                .antMatchers("/res/test1").hasAnyAuthority("SCOPE_read","SCOPE_all")
                .antMatchers("/res/test2").hasAnyAuthority("SCOPE_write666","SCOPE_all")
                // 其余请求都需要认证
                .anyRequest().authenticated()
                .and()
                // 异常处理
                .exceptionHandling(exceptionConfigurer -> exceptionConfigurer
                        // 拒绝访问
                        .accessDeniedHandler(accessDeniedHandler)
                        // 认证失败
                        .authenticationEntryPoint(authenticationEntryPoint)
                )
                // 资源服务
                .oauth2ResourceServer(resourceServer -> resourceServer
                        .accessDeniedHandler(accessDeniedHandler)
                        .authenticationEntryPoint(authenticationEntryPoint)
                        .jwt()
                )
                .build();
    }


    /**
     * JWT个性化解析
     *
     * @return
     */
    @Bean
    JwtAuthenticationConverter jwtAuthenticationConverter() {
        JwtAuthenticationConverter jwtAuthenticationConverter = new JwtAuthenticationConverter();
        JwtGrantedAuthoritiesConverter jwtGrantedAuthoritiesConverter = new JwtGrantedAuthoritiesConverter();
//        如果不按照规范  解析权限集合Authorities 就需要自定义key
//        jwtGrantedAuthoritiesConverter.setAuthoritiesClaimName("scopes");
//        OAuth2 默认前缀是 SCOPE_     Spring Security 是 ROLE_
//        jwtGrantedAuthoritiesConverter.setAuthorityPrefix("");
        jwtAuthenticationConverter.setJwtGrantedAuthoritiesConverter(jwtGrantedAuthoritiesConverter);
        // 用户名 可以放sub
        jwtAuthenticationConverter.setPrincipalClaimName(JwtClaimNames.SUB);
        return jwtAuthenticationConverter;
    }
}
