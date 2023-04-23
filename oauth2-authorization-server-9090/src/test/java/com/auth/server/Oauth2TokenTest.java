package com.auth.server;

import cn.hutool.http.HttpRequest;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.HashMap;
import java.util.Map;

@SpringBootTest
@Slf4j
public class Oauth2TokenTest {

    @Test
    public void Oauth2Token() throws JSONException {

        // oauth2授权码（授权码会过期，授权码一旦成功生成token就无法再次生成token，也就是说这个授权码是一次性的）
        String authorizationCode = "L1WhL6IyRDBnLARYL5_H1OmPnVG3oJOQ0kbd0xOC6DUZqFoOIJRBMove_v7LDD01oAjjATK-7DtkxMOdp5ihIRmco5JBa5usXrPUa7Iwmf7Wv7kqkY0Cc2ej3WHbu6d6";

//        JSONObject jsonObject = new JSONObject();
//        jsonObject.put("grant_type","authorization_code");
//        jsonObject.put("redirect_uri","http://www.baidu.com");
//        //授权码
//        jsonObject.put("code",authorizationCode);

        Map<String,Object> fromDataMap = new HashMap<>();
        fromDataMap.put("grant_type","authorization_code");
        fromDataMap.put("redirect_uri","http://www.baidu.com");
        //授权码
        fromDataMap.put("code",authorizationCode);

        String result = HttpRequest.post("http://127.0.0.1:9090/oauth2/token")
                .basicAuth("yzj_client", "yzj666888")
                .header("Content-Type", "multipart/form-data;charset=UTF-8")
                .form(fromDataMap)
                .execute().body();
        log.info("result={}",result);


    }

}
