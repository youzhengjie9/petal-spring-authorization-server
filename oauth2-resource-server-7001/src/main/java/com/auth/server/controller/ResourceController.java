package com.auth.server.controller;

import com.alibaba.fastjson2.JSONObject;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.time.LocalDateTime;

@RestController
@RequestMapping(path = "/res")
public class ResourceController {

    @GetMapping(path = "/test0")
    public String test0(){
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("time", LocalDateTime.now());
        jsonObject.put("name","resource-test0");
        return jsonObject.toJSONString();
    }

    @GetMapping(path = "/test1")
    @PreAuthorize("hasAnyAuthority('SCOPE_write666')")
    public String test1(){
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("time", LocalDateTime.now());
        jsonObject.put("name","resource-test1");
        return jsonObject.toJSONString();
    }

    @GetMapping(path = "/test2")
    @PreAuthorize("hasAnyAuthority('SCOPE_read','SCOPE_all')")
    public String test2(){
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("time", LocalDateTime.now());
        jsonObject.put("name","resource-test2");
        return jsonObject.toJSONString();
    }

}
