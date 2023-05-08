package com.auth.server.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.Set;

/**
 * 模拟支付宝的认证服务器属性配置
 *
 * @author youzhengjie
 * @date 2023/04/23 18:55:26
 */
@ConfigurationProperties(prefix = "alipay.authorization.server")
@EnableConfigurationProperties(AlipayAuthorizationServerProperties.class)
@Component
@Data
public class AlipayAuthorizationServerProperties {

    /**
     * 客户端id
     */
    private String clientId;

    /**
     * 客户端密码
     */
    private String clientPassword;

    /**
     * 客户端服务器的ip地址
     */
    private String clientIpAddr;

    /**
     * 重定向地址集合。
     * 认证成功后重定向的uri,不在这的地址将被拒绝。
     * 注意: 只能使用IP或域名，不能使用localhost
     */
    private Set<String> redirectUris;

    /**
     * 授权范围集合
     */
    private Set<String> scopes;

    /**
     * accessToken过期时间
     */
    private Duration accessTokenTimeToLive;

    /**
     * refreshToken过期时间
     */
    private Duration refreshTokenTimeToLive;

    /**
     * jks文件路径（默认是resources目录下）
     */
    private String jksPath;

    /**
     * jks证书别名
     */
    private String jksAlias;

    /**
     * jks证书的keystore密码
     */
    private String jksPassword;

    /**
     * 获取accessToken、refreshToken的url（格式为: http://认证服务器ip:认证服务器端口号/oauth2/token ）
     */
    private String oauth2TokenUrl;

    /**
     * 认证服务器地址
     */
    private String issuerUri;

}
