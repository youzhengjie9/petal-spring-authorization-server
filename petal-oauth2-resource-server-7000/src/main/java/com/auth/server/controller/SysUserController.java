package com.auth.server.controller;

import com.auth.server.entity.SysUser;
import com.auth.server.service.SysUserService;
import com.auth.server.utils.ResponseResult;
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
    public ResponseResult<SysUser> queryUserByUserName(@PathVariable("username") String username){

        return ResponseResult.ok(sysUserService.lambdaQuery().eq(SysUser::getUserName, username).one());
    }

    @GetMapping(path = "/queryUserByPhone/{phone}")
    public ResponseResult<SysUser> queryUserByPhone(@PathVariable("phone") String phone){

        return ResponseResult.ok(sysUserService.lambdaQuery().eq(SysUser::getPhone, phone).one());
    }

}
