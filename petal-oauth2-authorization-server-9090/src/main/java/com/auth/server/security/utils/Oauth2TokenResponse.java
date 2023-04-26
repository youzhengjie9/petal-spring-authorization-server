package com.auth.server.security.utils;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 通过oauth2授权码访问/oauth2/token接口
 * 作用: 封装/oauth2/token接口所返回的json数据
 * @author youzhengjie
 * @date 2023/04/23 18:14:59
 */
@NoArgsConstructor
@AllArgsConstructor
@Data
public class Oauth2TokenResponse implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * accessToken
     */
    private String access_token;

    /**
     * refreshToken
     */
    private String refresh_token;

    /**
     * 授权范围
     */
    private String scope;

    private String token_type;

    private Long expires_in;

    /**
     * 异常内容
     */
    private String error;

}
