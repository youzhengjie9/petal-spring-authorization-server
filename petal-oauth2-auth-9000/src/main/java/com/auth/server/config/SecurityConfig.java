package com.auth.server.config;

import com.auth.server.support.FormIdentityLoginConfigurer;
import com.auth.server.support.CustomDaoAuthenticationProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

/**
 * SpringSecurity安全配置类
 *
 * @author youzhengjie
 * @date 2023-05-05 09:24:58
 */
@EnableWebSecurity
public class SecurityConfig {

    /**
     * SpringSecurity默认的安全配置
     *
     * @param http
     * @return
     * @throws Exception
     */
    @Bean
    SecurityFilterChain defaultSecurityFilterChain(HttpSecurity http) throws Exception {
        http.authorizeRequests(authorizeRequests ->
                        // 开放自定义的部分端点
                         authorizeRequests.antMatchers("/token/*")
                        .permitAll()
                        // 除了上面的路径其余的都需要认证之后才能访问
                        .anyRequest()
                        .authenticated())
                 // 避免iframe同源无法登录
                .headers()
                .frameOptions()
                .sameOrigin()
                .and()
                 // 表单登录个性化
                .apply(new FormIdentityLoginConfigurer());
        // 处理 UsernamePasswordAuthenticationToken
        http.authenticationProvider(new CustomDaoAuthenticationProvider());
        return http.build();
    }

    /**
     * 开放部分路径，使其不需要认证就能访问
     *
     * @param http
     * @return
     * @throws Exception
     */
    @Bean
    @Order(0)
    SecurityFilterChain resources(HttpSecurity http) throws Exception {
        http.requestMatchers((matchers) -> matchers.antMatchers("/actuator/**", "/css/**", "/error"))
                .authorizeHttpRequests((authorize) -> authorize.anyRequest().permitAll())
                .requestCache()
                .disable()
                .securityContext()
                .disable()
                .sessionManagement()
                .disable();
        return http.build();
    }

}
