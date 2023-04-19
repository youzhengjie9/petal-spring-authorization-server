package com.auth.server.controller;

import com.alibaba.fastjson2.JSONObject;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;

@RestController
@RequestMapping(path = "/res")
public class ResourceController {

    @GetMapping(path = "/test3")
    public String test3(){
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("time", LocalDateTime.now());
        jsonObject.put("name","resource-test3");
        return jsonObject.toJSONString();
    }

    @GetMapping(path = "/test4")
    public String test4(){
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("time", LocalDateTime.now());
        jsonObject.put("name","resource-test4");
        return jsonObject.toJSONString();
    }

}
