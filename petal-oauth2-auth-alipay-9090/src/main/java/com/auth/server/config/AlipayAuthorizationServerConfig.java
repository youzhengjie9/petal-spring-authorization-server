package com.auth.server.config;

import com.auth.server.properties.AlipayAuthorizationServerProperties;
import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jose.jwk.source.JWKSource;
import com.nimbusds.jose.proc.SecurityContext;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
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
import java.util.Set;
import java.util.UUID;

/**
 * 模拟支付宝Spring Authorization Server认证服务器配置（授权码模式）
 * @author youzhengjie
 * @date 2023/04/21 23:13:41
 */
@Configuration(proxyBeanMethods = false)
public class AlipayAuthorizationServerConfig {

    private AlipayAuthorizationServerProperties authorizationServerProperties;

    /**
     * 自定义oauth2授权页面跳转接口（也可以使用oauth2自带的）
     */
    private static final String CUSTOM_CONSENT_PAGE_URI = "/oauth2/consent";

    @Autowired
    public void setAuthorizationServerProperties(AlipayAuthorizationServerProperties authorizationServerProperties) {
        this.authorizationServerProperties = authorizationServerProperties;
    }

    /**
     * Spring Authorization Server 授权配置
     */
    @Bean
    @Order(Ordered.HIGHEST_PRECEDENCE)
    SecurityFilterChain authorizationServerSecurityFilterChain(HttpSecurity http) throws Exception {
        // 定义授权服务配置器
        OAuth2AuthorizationServerConfigurer authorizationServerConfigurer =
                new OAuth2AuthorizationServerConfigurer();
        //配置自定义授权界面
        authorizationServerConfigurer
                .authorizationEndpoint(authorizationEndpoint ->
                        authorizationEndpoint.consentPage(CUSTOM_CONSENT_PAGE_URI));
        // 获取授权服务器相关的请求端点
        RequestMatcher requestMatcher =
                authorizationServerConfigurer.getEndpointsMatcher();
        http
                // 拦截对 授权服务器 相关端点的请求
                .requestMatcher(requestMatcher)
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
        // 查询客户端是否存在
        RegisteredClient registeredClient = registeredClientRepository.findByClientId(authorizationServerProperties.getClientId());
        // 数据库中没有该记录（说明该客户端没有被注册），则进行注册
        if (registeredClient == null) {
            registeredClient = this.registeredClientAuthorizationCode(authorizationServerProperties.getClientId());
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
                .accessTokenTimeToLive(authorizationServerProperties.getAccessTokenTimeToLive())
                // 是否启动refreshToken
                .reuseRefreshTokens(true)
                // refreshToken过期时间：7天（7天内当accessToken过期时，可以用refreshToken重新申请新的accessToken，不需要再认证）
                .refreshTokenTimeToLive(authorizationServerProperties.getRefreshTokenTimeToLive())
                .build();
        // 客户端相关配置
        ClientSettings clientSettings = ClientSettings.builder()
                // 是否需要用户授权确认
                .requireAuthorizationConsent(true)
                .build();

        RegisteredClient.Builder registeredClientBuilder = RegisteredClient
                // 客户端ID和密码
                .withId(UUID.randomUUID().toString())
                // Spring Security将使用它来识别哪个客户端正在尝试访问资源
                .clientId(clientId)
                // 客户端和服务器都知道的一个密码，它提供了两者之间的信任
                .clientSecret(new BCryptPasswordEncoder().encode(authorizationServerProperties.getClientPassword()))
                // 授权方法
                .clientAuthenticationMethod(ClientAuthenticationMethod.CLIENT_SECRET_BASIC)
                // 授权模式: 授权码模式
                .authorizationGrantType(AuthorizationGrantType.AUTHORIZATION_CODE)
                // refreshToken（授权码模式）
                .authorizationGrantType(AuthorizationGrantType.REFRESH_TOKEN)
                // 回调地址
                // JWT（Json Web Token）配置项
                .tokenSettings(tokenSettings)
                // 客户端配置项
                .clientSettings(clientSettings);
        //redirectUris
        Set<String> redirectUris = authorizationServerProperties.getRedirectUris();
        if(redirectUris != null && redirectUris.size()>0){
            for (String redirectUri : redirectUris) {
                // 授权范围（当前客户端的授权范围）
                registeredClientBuilder.redirectUri(redirectUri);
            }
        }
        //scopes
        Set<String> scopes = authorizationServerProperties.getScopes();
        if(scopes != null && scopes.size()>0){
            for (String scope : scopes) {
                // 授权范围（当前客户端的授权范围）
                registeredClientBuilder.scope(scope);
            }
        }
        return registeredClientBuilder.build();
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

        ClassPathResource resource = new ClassPathResource(authorizationServerProperties.getJksPath());
        KeyStore jks = KeyStore.getInstance("jks");
        char[] pin = authorizationServerProperties.getJksPassword().toCharArray();
        jks.load(resource.getInputStream(), pin);
        RSAKey rsaKey = RSAKey.load(jks, authorizationServerProperties.getJksAlias(), pin);
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
        return AuthorizationServerSettings.builder().issuer(authorizationServerProperties.getIssuerUri()).build();
    }


}
