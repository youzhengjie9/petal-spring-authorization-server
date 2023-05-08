package com.auth.server.config;

import com.auth.server.properties.JwtProperties;
import com.nimbusds.jose.jwk.RSAKey;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.io.ClassPathResource;
import org.springframework.security.oauth2.core.DelegatingOAuth2TokenValidator;
import org.springframework.security.oauth2.core.OAuth2TokenValidator;
import org.springframework.security.oauth2.jwt.*;

import java.io.InputStream;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.security.interfaces.RSAPublicKey;
import java.time.Duration;
import java.util.Collection;

/**
 * 自定义jwt解码器配置
 * proxyBeanMethods = false 每次调用都创建新的对象
 *
 * @author youzhengjie
 * @date 2023/04/25 00:17:35
 */
@EnableConfigurationProperties(JwtProperties.class)
@Configuration(proxyBeanMethods = false)
public class JwtDecoderConfig {

    private JwtProperties jwtProperties;

    @Autowired
    public void setJwtProperties(JwtProperties jwtProperties) {
        this.jwtProperties = jwtProperties;
    }

    /**
     * 校验jwt发行者issuer是否合法
     *
     * @return {@link JwtIssuerValidator}
     */
    @Bean
    JwtIssuerValidator jwtIssuerValidator() {
        return new JwtIssuerValidator(this.jwtProperties.getClaims().getIssuer());
    }

    /**
     * jwtToken委托校验器，集中校验的策略
     * @param tokenValidators the token validators
     * @return the delegating o auth 2 token validator
     */
    @Primary
    @Bean({"delegatingTokenValidator"})
    public DelegatingOAuth2TokenValidator<Jwt> delegatingTokenValidator(Collection<OAuth2TokenValidator<Jwt>> tokenValidators) {
        return new DelegatingOAuth2TokenValidator<>(tokenValidators);
    }

    /**
     * 作用: 校验jwt是否过期，如果该jwt过期了则会进入DefaultAuthenticationEntryPoint类，也就是认证失败（此时这个token访问不了受保护的接口）
     *
     * @return the jwt timestamp validator
     */
    @Bean
    public JwtTimestampValidator jwtTimestampValidator() {
        return new JwtTimestampValidator(Duration.ofSeconds(0L));
    }

    /**
     * 基于Nimbus的jwt解码器，并增加了一些自定义校验策略
     *
     * @param validator DelegatingOAuth2TokenValidator<Jwt> 委托token校验器
     * @return the jwt decoder
     */
    @SneakyThrows
    @Bean
    public JwtDecoder jwtDecoder(@Qualifier("delegatingTokenValidator")
                                         DelegatingOAuth2TokenValidator<Jwt> validator) {
        // 指定 X.509 类型的证书工厂
        CertificateFactory certificateFactory = CertificateFactory.getInstance("X.509");
        // 读取cer公钥证书来配置解码器
        String publicKeyLocation = this.jwtProperties.getCertInfo().getPublicKeyLocation();
        // 获取证书文件输入流
        ClassPathResource resource = new ClassPathResource(publicKeyLocation);
        InputStream inputStream = resource.getInputStream();
        // 得到证书
        X509Certificate certificate = (X509Certificate) certificateFactory.generateCertificate(inputStream);
        // 解析
        RSAKey rsaKey = RSAKey.parse(certificate);
        // 得到公钥
        RSAPublicKey key = rsaKey.toRSAPublicKey();
        // 构造解码器
        NimbusJwtDecoder nimbusJwtDecoder = NimbusJwtDecoder.withPublicKey(key).build();
        // 注入自定义JWT校验逻辑
        nimbusJwtDecoder.setJwtValidator(validator);
        return nimbusJwtDecoder;
    }
}
