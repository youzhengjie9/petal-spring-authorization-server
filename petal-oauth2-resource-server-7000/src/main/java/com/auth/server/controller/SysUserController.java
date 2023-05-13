package com.auth.server.controller;

import com.auth.server.entity.SysUser;
import com.auth.server.service.SysUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/sys/user")
public class SysUserController {

    private SysUserService sysUserService;

    @Autowired
    public void setSysUserService(SysUserService sysUserService) {
        this.sysUserService = sysUserService;
    }

    @GetMapping(path = "/queryUserByUserName/{username}")
    public SysUser queryUserByUserName(@PathVariable("username") String username){

        return sysUserService.lambdaQuery().eq(SysUser::getUserName, username).one();
    }



}
