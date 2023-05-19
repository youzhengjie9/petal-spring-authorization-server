package com.petal.oauth2.resource7001.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

/**
 * jwt属性配置
 *
 * @author youzhengjie
 * @date 2023/04/25 00:15:57
 */
@Data
@ConfigurationProperties(prefix = "jwt")
@EnableConfigurationProperties(JwtProperties.class)
public class JwtProperties {

    /**
     * 证书信息
     */
    private CertInfo certInfo;

    /**
     * 证书声明
     */
    private Claims claims;

    @Data
    public static class Claims {
        /**
         * 发证方
         */
        private String issuer;
    }

    @Data
    public static class CertInfo {
        /**
         * 证书存放位置
         */
        private String publicKeyLocation;

    }

}
