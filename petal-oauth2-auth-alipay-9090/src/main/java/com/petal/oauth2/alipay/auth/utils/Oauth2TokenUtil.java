package com.petal.oauth2.alipay.auth.utils;

import cn.hutool.http.HttpRequest;
import com.alibaba.fastjson2.JSON;
import com.auth.server.properties.AuthorizationServerProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.core.endpoint.OAuth2ParameterNames;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * oauth2的token工具类
 *
 * @author youzhengjie
 * @date 2023/04/23 18:29:31
 */
@Component
public class Oauth2TokenUtil {

    private AuthorizationServerProperties authorizationServerProperties;

    @Autowired
    public void setAuthorizationServerProperties(AuthorizationServerProperties authorizationServerProperties) {
        this.authorizationServerProperties = authorizationServerProperties;
    }

    /**
     * 通过oauth2授权码来获取accessToken和refreshToken
     *
     * @param authorizationCode oauth2授权码（授权码会过期，授权码一旦成功生成token就无法再次生成token，也就是说这个授权码是一次性的）
     * @return {@link Oauth2TokenResponse}
     */
    public Oauth2TokenResponse getOauth2Token(String authorizationCode){
        Map<String,Object> fromDataMap = new HashMap<>();
        //指定为授权码模式
        fromDataMap.put(OAuth2ParameterNames.GRANT_TYPE,"authorization_code");
        //指定回调uri
        fromDataMap.put(OAuth2ParameterNames.REDIRECT_URI,"http://127.0.0.1:8300");
        //授权码
        fromDataMap.put(OAuth2ParameterNames.CODE,authorizationCode);

        String result = HttpRequest.post(authorizationServerProperties.getOauth2TokenUrl())
                .basicAuth(
                        authorizationServerProperties.getClientId(),
                        authorizationServerProperties.getClientPassword()
                )
                .header("Content-Type", "multipart/form-data;charset=UTF-8")
                //封装form-data内容
                .form(fromDataMap)
                .execute().body();
        return JSON.parseObject(result,Oauth2TokenResponse.class);
    }


    /**
     * 通过refreshToken重新生成accessToken
     *
     * @param refreshToken refreshToken
     * @return {@link Oauth2TokenResponse}
     */
    public Oauth2TokenResponse refreshToken(String refreshToken){

        Map<String,Object> fromDataMap = new HashMap<>();
        //指定为refreshToken模式
        fromDataMap.put(OAuth2ParameterNames.GRANT_TYPE,"refresh_token");
        //指定refreshToken，通过这个refreshToken来刷新token
        fromDataMap.put(OAuth2ParameterNames.REFRESH_TOKEN,refreshToken);

        String result = HttpRequest.post(authorizationServerProperties.getOauth2TokenUrl())
                .basicAuth(
                        authorizationServerProperties.getClientId(),
                        authorizationServerProperties.getClientPassword()
                )
                .header("Content-Type", "multipart/form-data;charset=UTF-8")
                //封装form-data内容
                .form(fromDataMap)
                .execute().body();
        return JSON.parseObject(result,Oauth2TokenResponse.class);
    }


}
