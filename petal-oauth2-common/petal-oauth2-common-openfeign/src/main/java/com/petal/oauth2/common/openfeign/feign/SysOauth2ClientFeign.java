package com.petal.oauth2.common.openfeign.feign;

import com.petal.oauth2.common.base.constant.Oauth2Constant;
import com.petal.oauth2.common.base.utils.ResponseResult;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

@FeignClient(value = "petal-oauth2-resource-server-7000",path = "/client")
public interface SysOauth2ClientFeign {

    @GetMapping(value = "/getClientById/{clientId}")
    public ResponseResult getClientById(@PathVariable("clientId") String clientId,
                                        @RequestHeader(Oauth2Constant.ONLY_FEIGN_CALL_HEADER_NAME)
                                        String onlyFeignCallHeader);



}
