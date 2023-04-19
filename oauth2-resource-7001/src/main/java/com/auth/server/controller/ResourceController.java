package com.auth.server.controller;

import com.alibaba.fastjson2.JSONObject;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.time.LocalDateTime;

@RestController
@RequestMapping(path = "/res")
public class ResourceController {

    @GetMapping(path = "/test1")
    public String test1(){
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("time", LocalDateTime.now());
        jsonObject.put("name","resource-test1");
        return jsonObject.toJSONString();
    }

    @GetMapping(path = "/test2")
    public String test2(){
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("time", LocalDateTime.now());
        jsonObject.put("name","resource-test2");
        return jsonObject.toJSONString();
    }

}
