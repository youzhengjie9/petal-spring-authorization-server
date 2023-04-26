package com.auth.server.controller;

import com.alibaba.fastjson2.JSONObject;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.time.LocalDateTime;

/**
 * 资源控制器
 * <p>
 * "SCOPE_"是授权范围的前缀。（oauth2前缀有三个（SCOPE、ROLE、AUTH），从左到右粒度越来越细，oauth2默认前缀是SCOPE_）
 *
 * @author youzhengjie
 * @date 2023/04/26 00:13:39
 */
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
    //拥有“read或者all”授权范围任意一个就可以访问“/res/test1”
    @PreAuthorize("hasAnyAuthority('SCOPE_read','SCOPE_all')")
    public String test1(){
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("time", LocalDateTime.now());
        jsonObject.put("name","resource-test1");
        return jsonObject.toJSONString();
    }

    @GetMapping(path = "/test2")
    //拥有“write666或者all”授权范围任意一个就可以访问“/res/test2”
    @PreAuthorize("hasAnyAuthority('SCOPE_write666','SCOPE_all')")
    public String test2(){
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("time", LocalDateTime.now());
        jsonObject.put("name","resource-test2");
        return jsonObject.toJSONString();
    }

    @GetMapping(path = "/test3")
    @PreAuthorize("hasAnyAuthority('sys:test3')")
    public String test3(){
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("time", LocalDateTime.now());
        jsonObject.put("name","resource-test3");
        return jsonObject.toJSONString();
    }

    @GetMapping(path = "/test4")
    @PreAuthorize("hasAnyAuthority('sys:test4')")
    public String test4(){
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("time", LocalDateTime.now());
        jsonObject.put("name","resource-test4");
        return jsonObject.toJSONString();
    }

}
