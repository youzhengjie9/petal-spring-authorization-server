package com.petal.oauth2.client.utils;

import org.jetbrains.annotations.NotNull;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

/**
 * 请求微服务工具类
 *
 * @author youzhengjie
 * @date 2023/04/26 18:09:20
 */
@Component
public class RequestMicroService implements ApplicationContextAware {

    private ApplicationContext applicationContext;

    private RestTemplate restTemplate ;

    @Override
    public void setApplicationContext(@NotNull ApplicationContext applicationContext) throws BeansException {
        this.applicationContext=applicationContext;
        this.restTemplate = applicationContext.getBean(RestTemplate.class);
    }

    public String request(String url, OAuth2AuthorizedClient oAuth2AuthorizedClient){
        // 获取accessToken
        String tokenValue = oAuth2AuthorizedClient.getAccessToken().getTokenValue();
//        System.out.println("token= "+tokenValue);
        // 请求头
        HttpHeaders headers = new HttpHeaders();
        // 将accessToken放到请求头中
        headers.add("Authorization", "Bearer " + tokenValue);
        // 请求体
        HttpEntity<Object> httpEntity = new HttpEntity<>(headers);
        // 发起请求
        ResponseEntity<String> responseEntity;
        try {
            responseEntity = restTemplate.exchange(url, HttpMethod.GET, httpEntity, String.class);
        } catch (RestClientException e) {
            // e.getMessage() 信息格式：
            // 403 : "{"msg":"拒绝访问","uri":"/res2"}"
            // 解析，取出消息体 {"msg":"拒绝访问","uri":"/res2"}
            String str = e.getMessage();
            System.out.println(str);
            // 取两个括号中间的部分（包含两个括号）
            return str.substring(str.indexOf("{"), str.indexOf("}") + 1);
        }
        // 返回结果
        return responseEntity.getBody();
    }

}
