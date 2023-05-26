package com.petal.oauth2.resource7000.controller;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import com.petal.oauth2.common.base.entity.Message;
import com.petal.oauth2.common.base.utils.ResponseResult;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

/**
 * 资源控制器
 * <p>
 * "SCOPE_"是授权范围的前缀。（oauth2前缀有三个（SCOPE、ROLE、AUTH），从左到右粒度越来越细，oauth2默认前缀是SCOPE_）
 *
 * @author youzhengjie
 * @date 2023-05-26 09:23:56
 */
@RestController
@RequestMapping(path = "/res")
public class ResourceController {

    //----------不用权限-----------
    @GetMapping(path = "/test0")
    public String test0(){
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("time", LocalDateTime.now());
        jsonObject.put("name","resource-test0");
        return JSON.toJSONString(ResponseResult.ok(jsonObject.toJSONString()));
    }

    //---------需要SCOPE-----------------
    @GetMapping(path = "/test1")
    //拥有“read或者all”授权范围任意一个就可以访问“/res/test1”
    @PreAuthorize("@pms.hasPermission('SCOPE_read','SCOPE_all')")
    public String test1(){
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("time", LocalDateTime.now());
        jsonObject.put("name","resource-test1");
        return JSON.toJSONString(ResponseResult.ok(jsonObject.toJSONString()));
    }

    @GetMapping(path = "/test2")
    //拥有“server或者all”授权范围任意一个就可以访问“/res/test2”
    @PreAuthorize("@pms.hasPermission('SCOPE_server','SCOPE_all')")
    public String test2(){
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("time", LocalDateTime.now());
        jsonObject.put("name","resource-test2");
        return JSON.toJSONString(ResponseResult.ok(jsonObject.toJSONString()));
    }

    //----------需要RBAC中的菜单权限（自定义权限）-----------

    @GetMapping(path = "/test3")
//    @PreAuthorize("@pms.hasPermission('sys:test3')")
    @PreAuthorize("hasAnyAuthority('sys:test3')")
    public String test3(){
        System.out.println("test3");
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("time", LocalDateTime.now());
        jsonObject.put("name","resource-test3");
        return JSON.toJSONString(ResponseResult.ok(jsonObject.toJSONString()));
    }

    @GetMapping(path = "/test4")
//    @PreAuthorize("@pms.hasPermission('sys:test4')")
    @PreAuthorize("hasAnyAuthority('sys:test4')")
    public String test4(){
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("time", LocalDateTime.now());
        jsonObject.put("name","resource-test4");
        return JSON.toJSONString(ResponseResult.ok(jsonObject.toJSONString()));
    }


}
