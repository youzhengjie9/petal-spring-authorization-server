package com.auth.server.security;

import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jose.jwk.source.JWKSource;
import com.nimbusds.jose.proc.SecurityContext;
import lombok.SneakyThrows;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.ClientAuthenticationMethod;
import org.springframework.security.oauth2.server.authorization.JdbcOAuth2AuthorizationConsentService;
import org.springframework.security.oauth2.server.authorization.JdbcOAuth2AuthorizationService;
import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationConsentService;
import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationService;
import org.springframework.security.oauth2.server.authorization.client.JdbcRegisteredClientRepository;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClientRepository;
import org.springframework.security.oauth2.server.authorization.config.annotation.web.configurers.OAuth2AuthorizationServerConfigurer;
import org.springframework.security.oauth2.server.authorization.settings.AuthorizationServerSettings;
import org.springframework.security.oauth2.server.authorization.settings.ClientSettings;
import org.springframework.security.oauth2.server.authorization.settings.TokenSettings;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.RequestMatcher;

import java.security.KeyStore;
import java.time.Duration;
import java.util.UUID;

/**
 * Spring Authorization Server认证服务器配置
 *
 * @author youzhengjie
 * @date 2023/04/21 23:13:41
 */
@Configuration(proxyBeanMethods = false)
public class AuthorizationServerConfig {

    /**
     * 自定义oauth2授权页面跳转接口（也可以使用oauth2自带的）
     */
    private static final String CUSTOM_CONSENT_PAGE_URI = "/oauth2/consent";

    /**
     * Spring Authorization Server 授权配置
     */
    @Bean
    @Order(Ordered.HIGHEST_PRECEDENCE)
    SecurityFilterChain authorizationServerSecurityFilterChain(HttpSecurity http) throws Exception {
        // 定义授权服务配置器
        OAuth2AuthorizationServerConfigurer authorizationServerConfigurer =
                new OAuth2AuthorizationServerConfigurer();
        authorizationServerConfigurer
                .authorizationEndpoint(authorizationEndpoint ->
                        authorizationEndpoint.consentPage(CUSTOM_CONSENT_PAGE_URI));
        // 获取授权服务器相关的请求端点
        RequestMatcher requestMatcher =
                authorizationServerConfigurer.getEndpointsMatcher();
        http
                // 拦截对 授权服务器 相关端点的请求
                .requestMatcher(requestMatcher)
//                .userDetailsService(securityUserDetailsService)
                // 拦载到的请求需要认证确认（登录）
                .authorizeRequests(authorizeRequests ->
                        authorizeRequests.anyRequest().authenticated()
                )
                // 关闭csrf
                .csrf(csrf -> csrf
                        .ignoringRequestMatchers(requestMatcher)
                )
                .formLogin()
                .and()
                .logout()
                .and()
                // 应用认证服务器的配置
                .apply(authorizationServerConfigurer);
        return http.build();
    }

    /**
     * 注册客户端
     *
     * @param jdbcTemplate 操作数据库
     * @return 客户端仓库
     */
    @Bean
    RegisteredClientRepository registeredClientRepository(JdbcTemplate jdbcTemplate) {
        // Jdbc
        JdbcRegisteredClientRepository registeredClientRepository = new JdbcRegisteredClientRepository(jdbcTemplate);

        /*
         客户端在数据库中的几个记录字段的说明
         ------------------------------------------
         id：仅表示客户端在数据库中的这个记录
         client_id：唯一标示客户端；请求token时，以此作为客户端的账号
         client_name：客户端的名称，可以省略
         client_secret：密码
         */
        String clientId = "yzj_client";
        // 查询客户端是否存在
        RegisteredClient registeredClient = registeredClientRepository.findByClientId(clientId);
        // 数据库中没有该记录（说明该客户端没有被注册），则进行注册
        if (registeredClient == null) {
            registeredClient = this.registeredClientAuthorizationCode(clientId);
            registeredClientRepository.save(registeredClient);
        }
        // 返回客户端仓库
        return registeredClientRepository;
    }

    /**
     * 注册客户端（令牌申请方式：授权码模式）
     *
     * @param clientId 客户端ID
     * @return
     */
    private RegisteredClient registeredClientAuthorizationCode(final String clientId) {
        // jwtToken配置
        TokenSettings tokenSettings = TokenSettings.builder()
                // accessToken过期时间：2小时
                .accessTokenTimeToLive(Duration.ofHours(2))
                // 是否启动refreshToken
                .reuseRefreshTokens(true)
                // refreshToken过期时间：7天（7天内当accessToken过期时，可以用refreshToken重新申请新的accessToken，不需要再认证）
                .refreshTokenTimeToLive(Duration.ofDays(7))
                .build();
        // 客户端相关配置
        ClientSettings clientSettings = ClientSettings.builder()
                // 是否需要用户授权确认
                .requireAuthorizationConsent(true)
//                .requireAuthorizationConsent(false)
                .build();
        return RegisteredClient
                // 客户端ID和密码
                .withId(UUID.randomUUID().toString())
                // Spring Security将使用它来识别哪个客户端正在尝试访问资源
                .clientId(clientId)
                // 客户端和服务器都知道的一个密码，它提供了两者之间的信任
//                .clientSecret(PasswordEncoderFactories.createDelegatingPasswordEncoder().encode("123456"))
                .clientSecret("{noop}yzj666888")
                // 授权方法
                .clientAuthenticationMethod(ClientAuthenticationMethod.CLIENT_SECRET_BASIC)
                // 授权模式: 授权码模式
                .authorizationGrantType(AuthorizationGrantType.AUTHORIZATION_CODE)
                // refreshToken（授权码模式）
                .authorizationGrantType(AuthorizationGrantType.REFRESH_TOKEN)
                /* 回调地址：
                 * 授权服务器向当前客户端响应时调用下面地址；
                 * 不在此列的地址将被拒绝；
                 * 只能使用IP或域名，不能使用localhost
                 */
                .redirectUri("http://www.baidu.com")
                // 授权范围（当前客户端的授权范围）
                .scope("read")
                .scope("write")
                // JWT（Json Web Token）配置项
                .tokenSettings(tokenSettings)
                // 客户端配置项
                .clientSettings(clientSettings)
                .build();
    }

    /**
     * 作用: 将Token的发放的记录保存到数据库
     *
     * @param jdbcTemplate               操作数据库
     * @param registeredClientRepository 客户端仓库
     * @return 授权服务
     */
    @Bean
    public OAuth2AuthorizationService authAuthorizationService(
            JdbcTemplate jdbcTemplate,
            RegisteredClientRepository registeredClientRepository) {
        return new JdbcOAuth2AuthorizationService(jdbcTemplate, registeredClientRepository);
    }

    /**
     * 作用: 把资源拥有者授权确认操作保存到数据库
     * 资源拥有者（Resource Owner）对客户端的授权记录
     *
     * @param jdbcTemplate               操作数据库
     * @param registeredClientRepository 客户端仓库
     * @return
     */
    @Bean
    public OAuth2AuthorizationConsentService authAuthorizationConsentService(
            JdbcTemplate jdbcTemplate,
            RegisteredClientRepository registeredClientRepository) {
        return new JdbcOAuth2AuthorizationConsentService(jdbcTemplate, registeredClientRepository);
    }


    /**
     * 加载jwk资源
     * 用于生成令牌
     * @return
     */
    @SneakyThrows
    @Bean
    public JWKSource<SecurityContext> jwkSource() {
        // 证书的路径
        String path = "myjks.jks";
        // 证书别名
        String alias = "myjks";
        // keystore 密码
        String pass = "123456";

        ClassPathResource resource = new ClassPathResource(path);
        KeyStore jks = KeyStore.getInstance("jks");
        char[] pin = pass.toCharArray();
        jks.load(resource.getInputStream(), pin);
        RSAKey rsaKey = RSAKey.load(jks, alias, pin);

        JWKSet jwkSet = new JWKSet(rsaKey);
        return (jwkSelector, securityContext) -> jwkSelector.select(jwkSet);
    }

    /**
     * <p>认证服务器（oauth2-authorization-server-9090）元信息配置</p>
     * <p>
     * 授权服务器本身也提供了一个配置工具来配置其元信息，大多数都使用默认配置即可，唯一需要配置的其实只有（认证服务器oauth2-authorization-server-9090）的地址issuer
     * 在生产中这个地方应该配置为域名
     *
     * @return
     */
    @Bean
    public AuthorizationServerSettings authorizationServerSettings() {
        return AuthorizationServerSettings.builder().issuer("http://127.0.0.1:9090").build();
    }


}
