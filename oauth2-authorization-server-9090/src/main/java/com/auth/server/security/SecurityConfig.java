package com.auth.server.security;

import lombok.SneakyThrows;
import org.springframework.context.annotation.Bean;
import org.springframework.core.io.ClassPathResource;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.web.SecurityFilterChain;

import java.security.cert.Certificate;
import java.security.cert.CertificateFactory;
import java.security.interfaces.RSAPublicKey;

/**
 * SpringSecurity配置
 *
 * @author youzhengjie
 * @date 2023/04/21 21:43:17
 */
@EnableWebSecurity
public class SecurityConfig {

    /**
     * 设置密码加密方式
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
//        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
        return new BCryptPasswordEncoder();
    }

    /**
     * SpringSecurity默认的授权配置
     *
     * @param http
     * @return
     * @throws Exception
     */
    @Bean
    public SecurityFilterChain defaultSecurityFilterChain(HttpSecurity http) throws Exception {
        http
                .authorizeRequests(authorizeRequests ->
                        authorizeRequests.antMatchers("/login").permitAll()
                                .anyRequest().authenticated()
                )
                //设置表单登录
                .formLogin(fromlogin ->
                        fromlogin.loginPage("/login").permitAll()
                )
                .logout()
                .and()
//                //关闭csrf
//                .csrf()
//                .disable()
                .oauth2ResourceServer().jwt();
        ;
        return http.build();
    }

    /**
     * jwt解码器
     * 作用: 客户端认证授权后，需要访问user信息，解码器可以从令牌中解析出user信息
     */
    @SneakyThrows
    @Bean
    public JwtDecoder jwtDecoder() {
        CertificateFactory certificateFactory = CertificateFactory.getInstance("x.509");
        // 读取cer公钥证书来配置解码器
        ClassPathResource resource = new ClassPathResource("myjks.cer");
        Certificate certificate = certificateFactory.generateCertificate(resource.getInputStream());
        RSAPublicKey publicKey = (RSAPublicKey) certificate.getPublicKey();
        return NimbusJwtDecoder.withPublicKey(publicKey).build();
    }

    /**
     * 开放一些端点的访问控制
     * 作用: 不需要认证就可以访问的端口
     * @return
     */
    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        return web -> web.ignoring().antMatchers("/actuator/health", "/actuator/info");
    }

}
