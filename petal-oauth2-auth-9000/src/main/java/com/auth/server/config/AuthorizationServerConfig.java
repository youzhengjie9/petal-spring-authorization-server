package com.auth.server.config;

import com.auth.server.constant.SecurityConstants;
import com.auth.server.handler.CustomAuthenticationFailureHandler;
import com.auth.server.handler.CustomAuthenticationSuccessHandler;
import com.auth.server.properties.AuthorizationServerProperties;
import com.auth.server.support.CustomeOAuth2AccessTokenGenerator;
import com.auth.server.support.CustomeOAuth2TokenCustomizer;
import com.auth.server.support.FormIdentityLoginConfigurer;
import com.auth.server.support.password.OAuth2ResourceOwnerPasswordAuthenticationConverter;
import com.auth.server.support.password.CustomOAuth2PasswordAuthenticationProvider;
import com.auth.server.support.sms.OAuth2ResourceOwnerSmsAuthenticationConverter;
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
import org.springframework.security.authentication.AuthenticationManager;
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
import org.springframework.security.oauth2.server.authorization.config.annotation.web.configuration.OAuth2AuthorizationServerConfiguration;
import org.springframework.security.oauth2.server.authorization.config.annotation.web.configurers.OAuth2AuthorizationServerConfigurer;
import org.springframework.security.oauth2.server.authorization.settings.AuthorizationServerSettings;
import org.springframework.security.oauth2.server.authorization.settings.ClientSettings;
import org.springframework.security.oauth2.server.authorization.settings.TokenSettings;
import org.springframework.security.oauth2.server.authorization.token.DelegatingOAuth2TokenGenerator;
import org.springframework.security.oauth2.server.authorization.token.OAuth2RefreshTokenGenerator;
import org.springframework.security.oauth2.server.authorization.token.OAuth2TokenGenerator;
import org.springframework.security.oauth2.server.authorization.web.authentication.*;
import org.springframework.security.web.DefaultSecurityFilterChain;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationConverter;

import java.security.KeyStore;
import java.util.Arrays;
import java.util.Set;
import java.util.UUID;

/**
 * Spring Authorization Server认证服务器配置（帐号密码、手机号登录）
 * @author youzhengjie
 * @date 2023-05-05 09:31:53
 */
@Configuration(proxyBeanMethods = false)
public class AuthorizationServerConfig {

    private AuthorizationServerProperties authorizationServerProperties;

    private OAuth2AuthorizationService authorizationService;

    /**
     * 自定义oauth2授权页面跳转接口（也可以使用oauth2自带的）
     */
    private static final String CUSTOM_CONSENT_PAGE_URI = "/oauth2/consent";

    @Autowired
    public void setAuthorizationServerProperties(AuthorizationServerProperties authorizationServerProperties) {
        this.authorizationServerProperties = authorizationServerProperties;
    }

    @Autowired
    public void setAuthorizationService(OAuth2AuthorizationService authorizationService) {
        this.authorizationService = authorizationService;
    }

    /**
     * Spring Authorization Server 授权配置
     */
    @Bean
    @Order(Ordered.HIGHEST_PRECEDENCE)
    public SecurityFilterChain authorizationServerSecurityFilterChain(HttpSecurity http) throws Exception {

        // OAuth 2.1 默认配置
        // 缺省配置：authorizeRequests.anyRequest().authenticated()、
        // csrf.ignoringRequestMatchers(endpointsMatcher) 等等
        OAuth2AuthorizationServerConfiguration.applyDefaultSecurity(http);

        // 使用 HttpSecurity 获取 OAuth 2.1 配置中的 OAuth2AuthorizationServerConfigurer 对象
        OAuth2AuthorizationServerConfigurer authorizationServerConfigurer = http
                .getConfigurer(OAuth2AuthorizationServerConfigurer.class);

        authorizationServerConfigurer
                // 自定义认证授权端点
                .tokenEndpoint((tokenEndpoint) -> {
                    // 注入自定义的授权认证Converter
                    tokenEndpoint.accessTokenRequestConverter(accessTokenRequestConverter())
                            // 自定义登录成功处理器
                            .accessTokenResponseHandler(new CustomAuthenticationSuccessHandler())
                            // 自定义登录失败处理器
                            .errorResponseHandler(new CustomAuthenticationFailureHandler());
                })
                // 自定义客户端认证
                .clientAuthentication(oAuth2ClientAuthenticationConfigurer ->
                        oAuth2ClientAuthenticationConfigurer
                                // 自定义登录失败处理器
                                .errorResponseHandler(new CustomAuthenticationFailureHandler()))
                .authorizationEndpoint(authorizationEndpoint -> authorizationEndpoint
                        // 自定义授权页
                        .consentPage(SecurityConstants.CUSTOM_CONSENT_PAGE_URI));

        DefaultSecurityFilterChain securityFilterChain = authorizationServerConfigurer
                // redis存储token的实现
                .authorizationService(authorizationService)
                .authorizationServerSettings(
                        AuthorizationServerSettings.builder().issuer("http://petal.oauth2.com:9000").build())
                .and()
                // 配置表单个性化登录配置
                .apply(new FormIdentityLoginConfigurer())
                .and()
                .build();
        // 注入自定义授权模式实现
        addCustomOAuth2GrantAuthenticationProvider(http);
        return securityFilterChain;
    }

    /**
     * 令牌生成规则实现
     * </br>
     * client:username:uuid
     * @return OAuth2TokenGenerator
     */
    @Bean
    public OAuth2TokenGenerator oAuth2TokenGenerator() {
        // 自定义Oauth2的accessToken生成规则对象
        CustomeOAuth2AccessTokenGenerator accessTokenGenerator = new CustomeOAuth2AccessTokenGenerator();
        // 设置Token增加关联用户信息
        accessTokenGenerator.setAccessTokenCustomizer(new CustomeOAuth2TokenCustomizer());
        return new DelegatingOAuth2TokenGenerator(accessTokenGenerator, new OAuth2RefreshTokenGenerator());
    }

    /**
     * 配置各种请求转换器
     * @return DelegatingAuthenticationConverter
     */
    private AuthenticationConverter accessTokenRequestConverter() {
        //DelegatingAuthenticationConverter在解析请求时会遍历 AuthenticationConverter列表，
        //当某个 AuthenticationConverter 解析成功时，立即返回，这也能确定此请求是什么认证方式，后续再执行对应的认证逻辑
        return new DelegatingAuthenticationConverter(Arrays.asList(
                new OAuth2ResourceOwnerPasswordAuthenticationConverter(),
                new OAuth2ResourceOwnerSmsAuthenticationConverter(),
                // 将 HttpServletRequest中提取的OAuth2的refreshToken授予的访问令牌请求 ===> OAuth2RefreshTokenAuthenticationToken
                new OAuth2RefreshTokenAuthenticationConverter(),
                // 将 HttpServletRequest中提取的OAuth2的客户端凭据授予的访问令牌请求 ===>  OAuth2RefreshTokenAuthenticationToken
                new OAuth2ClientCredentialsAuthenticationConverter(),
                // 将 HttpServletRequest中提取OAuth2的授权码授予的访问令牌请求 ===> OAuth2AuthorizationCodeAuthenticationToken
                new OAuth2AuthorizationCodeAuthenticationConverter(),
                // 将 HttpServletRequest中提取OAuth2的授权码授予的授权请求 ===> OAuth2AuthorizationCodeRequestAuthenticationToken
                new OAuth2AuthorizationCodeRequestAuthenticationConverter()));
    }

    /**
     * 注入授权模式实现提供方
     *
     * 1. 密码模式 </br>
     * 2. 短信登录 </br>
     *
     */
    @SuppressWarnings("unchecked")
    private void addCustomOAuth2GrantAuthenticationProvider(HttpSecurity http) {
        AuthenticationManager authenticationManager = http.getSharedObject(AuthenticationManager.class);
        OAuth2AuthorizationService authorizationService = http.getSharedObject(OAuth2AuthorizationService.class);

        CustomOAuth2PasswordAuthenticationProvider resourceOwnerPasswordAuthenticationProvider = new CustomOAuth2PasswordAuthenticationProvider(
                authenticationManager, authorizationService, oAuth2TokenGenerator());

        OAuth2ResourceOwnerSmsAuthenticationProvider resourceOwnerSmsAuthenticationProvider = new OAuth2ResourceOwnerSmsAuthenticationProvider(
                authenticationManager, authorizationService, oAuth2TokenGenerator());

        // 处理 UsernamePasswordAuthenticationToken
        http.authenticationProvider(new PigDaoAuthenticationProvider());
        // 处理 CustomOAuth2PasswordAuthenticationToken
        http.authenticationProvider(resourceOwnerPasswordAuthenticationProvider);
        // 处理 OAuth2ResourceOwnerSmsAuthenticationToken
        http.authenticationProvider(resourceOwnerSmsAuthenticationProvider);
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
