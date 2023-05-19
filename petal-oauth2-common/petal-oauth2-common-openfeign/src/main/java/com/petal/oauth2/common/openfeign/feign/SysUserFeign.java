package com.petal.oauth2.common.openfeign.feign;

import com.petal.oauth2.common.base.entity.SysUser;
import com.petal.oauth2.common.base.utils.ResponseResult;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(value = "petal-oauth2-resource-server-7000",path = "/sys/user")
public interface SysUserFeign {

    @GetMapping(path = "/queryUserByUserName/{username}")
    public ResponseResult<SysUser> queryUserByUserName(@PathVariable("username") String username);

    @GetMapping(path = "/queryUserByPhone/{phone}")
    public ResponseResult<SysUser> queryUserByPhone(@PathVariable("phone") String phone);

}
