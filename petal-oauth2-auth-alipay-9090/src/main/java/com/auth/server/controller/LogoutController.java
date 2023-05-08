package com.auth.server.controller;

import com.auth.server.enums.ResponseType;
import com.auth.server.utils.ResponseResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 注销控制器
 *
 * @author youzhengjie
 * @date 2023/04/29 18:07:03
 */
@RestController
public class LogoutController {

    @GetMapping("/logout")
    public String logout(){
        return ResponseResult.build(ResponseType.LOGOUT_SUCCESS).toJSONString();
    }

}
