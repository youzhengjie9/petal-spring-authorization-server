package com.auth.server;

import com.auth.server.security.utils.Oauth2TokenResponse;
import com.auth.server.security.utils.Oauth2TokenUtil;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONException;
import org.junit.Test;
import org.junit.platform.commons.util.StringUtils;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@SpringBootTest
@RunWith(SpringRunner.class)
@Slf4j
public class Oauth2TokenTest {

    private Oauth2TokenUtil oauth2TokenUtil;

    @Autowired
    public void setOauth2TokenUtil(Oauth2TokenUtil oauth2TokenUtil) {
        this.oauth2TokenUtil = oauth2TokenUtil;
    }

    @Test
    public void Oauth2Token() throws JSONException {

        // oauth2授权码（授权码会过期，授权码一旦成功生成token就无法再次生成token，也就是说这个授权码是一次性的）
        String authorizationCode = "FoDybuocbOVOPsHHhq3gy7TKWlJIeQWDjKYGdFvdUjvT8xovi9nXcylUA-eUwOQGy_aaK3d9UjAfspDfvh9x3pnbjHUX1lhhR1MsQU9yoJtxK7bs0QsSSH3idRWec92J";

        Oauth2TokenResponse oauth2TokenResponse = oauth2TokenUtil.getOauth2Token(authorizationCode);
        //如果没有异常
        if(StringUtils.isBlank(oauth2TokenResponse.getError())){
            log.info("accessToken={}",oauth2TokenResponse.getAccess_token());
            log.info("refreshToken={}",oauth2TokenResponse.getRefresh_token());
        }else {
            log.info("error={}",oauth2TokenResponse.getError());
        }

    }

}
