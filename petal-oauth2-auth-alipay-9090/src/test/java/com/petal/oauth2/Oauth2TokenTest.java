package com.petal.oauth2;

import com.petal.oauth2.auth.alipay.utils.Oauth2TokenResponse;
import com.petal.oauth2.auth.alipay.utils.Oauth2TokenUtil;
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
        String authorizationCode = "YsBbZDT_mbMIOKto3gKAd3qHrusiu3KmnpTwIc9C7ahLcKQoH46Nc0x9r8_SnW6GjuxqmUmqvSd-qG9koB7ADePBRBxsBwoqPzIJ8jn2qFL0tdwquscyXicvA_pNveij&state=BuVNO_Ei4l0J3BsS9ZiP3mtxwluSmlMl6cr8d2iQwlE%3D";

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
