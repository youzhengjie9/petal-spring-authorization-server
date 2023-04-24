package com.auth.server.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * 登录控制器
 *
 * @author youzhengjie
 * @date 2023/04/24 09:43:05
 */
@Controller
public class LoginController {

    /**
     * 跳转自定义登录页面
     *
     * @return {@link String}
     */
    @GetMapping("/login")
    public String loginPage(){
        return "login";
    }

}
