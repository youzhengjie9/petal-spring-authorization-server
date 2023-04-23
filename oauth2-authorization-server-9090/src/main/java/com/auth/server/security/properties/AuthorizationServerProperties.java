package com.auth.server.security.properties;

import lombok.Data;
import lombok.Getter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.Set;

/**
 * 认证服务器属性配置
 *
 * @author youzhengjie
 * @date 2023/04/23 18:55:26
 */
@ConfigurationProperties(prefix = "authorization.server")
@EnableConfigurationProperties(AuthorizationServerProperties.class)
@Getter
@Component
public class AuthorizationServerProperties {

    /**
     * 客户端id
     */
    private String clientId;

    /**
     * 客户端密码
     */
    private String clientPassword;

    /**
     * 认证成功后重定向的uri
     */
    private String redirectUri;

    /**
     * 授权范围集合
     */
    private Set<String> scopes;

    /**
     * jks文件路径
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
     * oauth2Token的url
     */
    private String oauth2TokenUrl;

}
