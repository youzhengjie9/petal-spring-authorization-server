package com.auth.server.feign;

import com.auth.server.entity.SysOauthClient;
import com.auth.server.utils.ResponseResult;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@FeignClient(value = "petal-oauth2-resource-server-7000",path = "/client")
public interface SysOauth2ClientFeign {

    @GetMapping("/getClientById/{clientId}")
    public ResponseResult getClientById(@PathVariable("clientId") String clientId);





}
