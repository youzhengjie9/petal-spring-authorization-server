package com.auth.server.controller;

import com.auth.server.entity.Message;
import com.auth.server.utils.RequestMicroService;
import com.auth.server.utils.ResponseResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.annotation.RegisteredOAuth2AuthorizedClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(path = "/client")
public class ClientController {

    private RequestMicroService requestMicroService;

    /**
     * 资源服务器地址（后期可以用注册中心省去写ip地址）
     */
    private static final String RESOURCE_SERVER_ADDRESS = "http://127.0.0.1:7001";

    @Autowired
    public void setRequestMicroService(RequestMicroService requestMicroService) {
        this.requestMicroService = requestMicroService;
    }

    //----------不用权限-----------
    @GetMapping(path = "/test0")
    public String test0(@RegisteredOAuth2AuthorizedClient
                                        OAuth2AuthorizedClient oAuth2AuthorizedClient){
        final String requestPath = RESOURCE_SERVER_ADDRESS + "/res/test0";
        return requestMicroService.request(requestPath, oAuth2AuthorizedClient);
    }

    //---------需要SCOPE-----------------
    @GetMapping(path = "/test1")
    public String test1(@RegisteredOAuth2AuthorizedClient
                                            OAuth2AuthorizedClient oAuth2AuthorizedClient){
        final String requestPath = RESOURCE_SERVER_ADDRESS + "/res/test1";
        return requestMicroService.request(requestPath,oAuth2AuthorizedClient);
    }

    @GetMapping(path = "/test2")
    public String test2(@RegisteredOAuth2AuthorizedClient
                                            OAuth2AuthorizedClient oAuth2AuthorizedClient){
        final String requestPath = RESOURCE_SERVER_ADDRESS + "/res/test2";
        return requestMicroService.request(requestPath,oAuth2AuthorizedClient);
    }

    //----------需要RBAC中的菜单权限（自定义权限）-----------

    @GetMapping(path = "/test3")
    public String test3(@RegisteredOAuth2AuthorizedClient
                                            OAuth2AuthorizedClient oAuth2AuthorizedClient){
        final String requestPath = RESOURCE_SERVER_ADDRESS + "/res/test3";
        return requestMicroService.request(requestPath,oAuth2AuthorizedClient);
    }

    @GetMapping(path = "/test4")
    public String test4(@RegisteredOAuth2AuthorizedClient
                                            OAuth2AuthorizedClient oAuth2AuthorizedClient){
        final String requestPath = RESOURCE_SERVER_ADDRESS + "/res/test4";
        return requestMicroService.request(requestPath,oAuth2AuthorizedClient);
    }

    //---------封装响应实体类------------------
    @GetMapping(path = "/test5")
    public String test5(@RegisteredOAuth2AuthorizedClient
                                             OAuth2AuthorizedClient oAuth2AuthorizedClient){
        final String requestPath = RESOURCE_SERVER_ADDRESS + "/res/test5";
        return requestMicroService.request(requestPath,oAuth2AuthorizedClient);
    }

    @GetMapping(path = "/test6")
    public String test6(@RegisteredOAuth2AuthorizedClient
                                             OAuth2AuthorizedClient oAuth2AuthorizedClient){
        final String requestPath = RESOURCE_SERVER_ADDRESS + "/res/test6";
        return requestMicroService.request(requestPath,oAuth2AuthorizedClient);
    }

    //----------集合------
    @GetMapping(path = "/test7")
    public String test7(@RegisteredOAuth2AuthorizedClient
                                                   OAuth2AuthorizedClient oAuth2AuthorizedClient){
        final String requestPath = RESOURCE_SERVER_ADDRESS + "/res/test7";
        return requestMicroService.request(requestPath,oAuth2AuthorizedClient);
    }


}
