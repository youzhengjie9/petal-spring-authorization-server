package com.auth.server.controller;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * oauth2控制器
 *
 * @author youzhengjie
 * @date 2023/04/22 00:25:44
 */
@RestController
@RequestMapping("/oauth2")
public class Oauth2Controller {

    /**
     * 获取用户信息
     * @return
     */
    @GetMapping("/userinfo")
    public Authentication oauth2UserInfo(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if(authentication == null){
            throw new RuntimeException("无有效认证用户！");
        }
        return authentication;
    }

}
