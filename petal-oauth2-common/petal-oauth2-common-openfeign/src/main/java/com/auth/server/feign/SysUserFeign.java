package com.auth.server.feign;

import com.auth.server.entity.SysUser;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(value = "petal-oauth2-resource-server-7000",path = "/sys/user")
public interface SysUserFeign {

    @GetMapping(path = "/queryUserByUserName/{username}")
    public SysUser queryUserByUserName(@PathVariable("username") String username);


}
