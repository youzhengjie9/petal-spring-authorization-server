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
    public void getOauth2TokenTest() throws JSONException {

        // oauth2授权码（授权码会过期，授权码一旦成功生成token就无法再次生成token，也就是说这个授权码是一次性的）
        String authorizationCode = "H7PD-HGXYvttCeYB0SDv7m_5jo_lvKI0A5H-K_U-w-8oLc0NIV6nzOQBkJHXD2HKjy-uE86ccEiDRGvBQDFxxixG6FEkip8SzbV18NQ6OuVpKV5KcqBSY7Ihpknn0B_N";

        Oauth2TokenResponse oauth2TokenResponse = oauth2TokenUtil.getOauth2Token(authorizationCode);
        //如果没有异常
        if(StringUtils.isBlank(oauth2TokenResponse.getError())){
            log.info("accessToken={}",oauth2TokenResponse.getAccess_token());
            log.info("refreshToken={}",oauth2TokenResponse.getRefresh_token());
        }else {
            log.info("error={}",oauth2TokenResponse.getError());
        }

    }

    @Test
    public void refreshTokenTest(){

        // refreshToken
        String refreshToken = "W8jboQHu-5OZ7UqFC0qZFyjY83TzShrY_PoQPM1Jr_oDTYvwds1QlPm-CUwA3r_UY111Sozgh-jXn5aVsDL7FaEzALQ8vf7U3_RlM3FuhR9lBzOjG9a2PLzUMHrGMMcj";

        Oauth2TokenResponse oauth2TokenResponse = oauth2TokenUtil.refreshToken(refreshToken);
        //如果没有异常
        if(StringUtils.isBlank(oauth2TokenResponse.getError())){
            log.info("accessToken={}",oauth2TokenResponse.getAccess_token());
            log.info("refreshToken={}",oauth2TokenResponse.getRefresh_token());
        }else {
            log.info("error={}",oauth2TokenResponse.getError());
        }

    }


}
