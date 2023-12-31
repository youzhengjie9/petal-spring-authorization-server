package com.petal.oauth2.common.openfeign.feign;

import com.petal.oauth2.common.base.constant.Oauth2Constant;
import com.petal.oauth2.common.base.entity.SysUser;
import com.petal.oauth2.common.base.utils.ResponseResult;
import com.petal.oauth2.common.openfeign.constant.ApplicationNameConstant;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient(value = ApplicationNameConstant.PETAL_OAUTH2_RESOURCE_SERVER,path = "/sys/user")
public interface SysUserFeign {

    @GetMapping(path = "/queryUserByUserName/{username}")
    public ResponseResult<SysUser> queryUserByUserName(@PathVariable("username") String username,
                                                       @RequestHeader(Oauth2Constant.ONLY_FEIGN_CALL_HEADER_NAME)
                                                       String onlyFeignCallHeader);

    @GetMapping(path = "/queryUserByPhone/{phone}")
    public ResponseResult<SysUser> queryUserByPhone(@PathVariable("phone") String phone,
                                                    @RequestHeader(Oauth2Constant.ONLY_FEIGN_CALL_HEADER_NAME)
                                                    String onlyFeignCallHeader);

}
