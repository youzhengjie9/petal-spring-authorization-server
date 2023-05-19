package com.petal.oauth2.resource7001.controller;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import com.auth.server.entity.Message;
import com.auth.server.utils.ResponseResult;
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
 * @date 2023/04/26 00:13:39
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
    @PreAuthorize("hasAnyAuthority('SCOPE_read','SCOPE_all')")
    public String test1(){
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("time", LocalDateTime.now());
        jsonObject.put("name","resource-test1");
        return JSON.toJSONString(ResponseResult.ok(jsonObject.toJSONString()));
    }

    @GetMapping(path = "/test2")
    //拥有“write666或者all”授权范围任意一个就可以访问“/res/test2”
    @PreAuthorize("hasAnyAuthority('SCOPE_write666','SCOPE_all')")
    public String test2(){
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("time", LocalDateTime.now());
        jsonObject.put("name","resource-test2");
        return JSON.toJSONString(ResponseResult.ok(jsonObject.toJSONString()));
    }

    //----------需要RBAC中的菜单权限（自定义权限）-----------

    @GetMapping(path = "/test3")
    @PreAuthorize("hasAnyAuthority('sys:test3')")
    public String test3(){
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("time", LocalDateTime.now());
        jsonObject.put("name","resource-test3");
        return JSON.toJSONString(ResponseResult.ok(jsonObject.toJSONString()));
    }

    @GetMapping(path = "/test4")
    @PreAuthorize("hasAnyAuthority('sys:test4')")
    public String test4(){
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("time", LocalDateTime.now());
        jsonObject.put("name","resource-test4");
        return JSON.toJSONString(ResponseResult.ok(jsonObject.toJSONString()));
    }

    //---------封装响应实体类------------------
    @GetMapping(path = "/test5")
    public String test5(){
        return JSON.toJSONString(ResponseResult.ok(new Message(LocalDateTime.now(),"resource--test5")));
    }

    @GetMapping(path = "/test6")
    @PreAuthorize("hasAnyAuthority('sys:test6')")
    public String test6(){
        return JSON.toJSONString(ResponseResult.ok(new Message(LocalDateTime.now(),"resource--test6")));
    }

    //----------集合------
    @GetMapping(path = "/test7")
    public String test7(){
        List<Message> messageList = Arrays.asList(
                new Message(LocalDateTime.now(), "test7-----1"),
                new Message(LocalDateTime.now(), "test7-----2"),
                new Message(LocalDateTime.now(), "test7-----3")
        );
        return JSON.toJSONString(ResponseResult.ok(messageList));
    }




}
