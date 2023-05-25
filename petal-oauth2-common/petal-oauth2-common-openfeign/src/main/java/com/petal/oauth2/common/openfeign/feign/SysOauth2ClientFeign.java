package com.petal.oauth2.common.openfeign.feign;

import com.petal.oauth2.common.base.constant.Oauth2Constant;
import com.petal.oauth2.common.base.utils.ResponseResult;
import com.petal.oauth2.common.openfeign.constant.ApplicationNameConstant;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

@FeignClient(value = ApplicationNameConstant.PETAL_OAUTH2_RESOURCE_SERVER,path = "/client")
public interface SysOauth2ClientFeign {

    @GetMapping(value = "/getClientById/{clientId}")
    public ResponseResult getClientById(@PathVariable("clientId") String clientId,
                                        @RequestHeader(Oauth2Constant.ONLY_FEIGN_CALL_HEADER_NAME)
                                        String onlyFeignCallHeader);



}
