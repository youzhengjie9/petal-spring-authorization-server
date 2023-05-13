package com.auth.server.endpoint;

import cn.hutool.core.util.StrUtil;
import com.auth.server.entity.SysOauthClient;
import com.auth.server.enums.ResponseType;
import com.auth.server.exception.OAuthClientException;
import com.auth.server.feign.SysOauth2ClientFeign;
import com.auth.server.support.handler.CustomAuthenticationFailureHandler;
import com.auth.server.utils.OAuth2EndpointUtils;
import com.auth.server.utils.OAuth2ErrorCodesExpand;
import com.auth.server.utils.ResponseResult;
import com.auth.server.utils.SpringContextHolder;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServletServerHttpResponse;
import org.springframework.security.authentication.event.LogoutSuccessEvent;
import org.springframework.security.oauth2.core.OAuth2AccessToken;
import org.springframework.security.oauth2.core.endpoint.OAuth2AccessTokenResponse;
import org.springframework.security.oauth2.core.endpoint.OAuth2ParameterNames;
import org.springframework.security.oauth2.core.http.converter.OAuth2AccessTokenResponseHttpMessageConverter;
import org.springframework.security.oauth2.server.authorization.OAuth2Authorization;
import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationService;
import org.springframework.security.oauth2.server.authorization.OAuth2TokenType;
import org.springframework.security.oauth2.server.resource.InvalidBearerTokenException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.security.Principal;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Token的端点控制器
 *
 * @author youzhengjie
 * @date 2023/04/22 00:25:44
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/token")
public class TokenEndPointController {

    private final HttpMessageConverter<OAuth2AccessTokenResponse> accessTokenHttpResponseConverter =
            new OAuth2AccessTokenResponseHttpMessageConverter();

    private final AuthenticationFailureHandler authenticationFailureHandler =
            new CustomAuthenticationFailureHandler();

    private final OAuth2AuthorizationService authorizationService;

    private final SysOauth2ClientFeign sysOauth2ClientFeign;

    private RedisTemplate redisTemplate;

    @Autowired
    public void setRedisTemplate(RedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
    }


    /**
     * 认证页面
     *
     * @param modelAndView
     * @param error 表单登录失败处理回调的错误信息
     * @return ModelAndView
     */
    @GetMapping("/login")
    public ModelAndView login(ModelAndView modelAndView, @RequestParam(required = false) String error) {
        modelAndView.setViewName("login");
        modelAndView.addObject("error", error);
        return modelAndView;
    }

    /**
     * 授权
     *
     * @param principal    主要
     * @param modelAndView 模型和视图
     * @param clientId     客户机id
     * @param scope        范围
     * @param state        状态
     * @return {@link ModelAndView}
     */
    @GetMapping("/consent")
    public ModelAndView consent(Principal principal, ModelAndView modelAndView,
                                @RequestParam(OAuth2ParameterNames.CLIENT_ID) String clientId,
                                @RequestParam(OAuth2ParameterNames.SCOPE) String scope,
                                @RequestParam(OAuth2ParameterNames.STATE) String state) {

        ResponseResult responseResult = sysOauth2ClientFeign.getClientById(clientId);
        if(responseResult != null){
            Object data = responseResult.getData();
            if(data != null){
                SysOauthClient sysOauthClient = (SysOauthClient) data;
                Set<String> authorizedScopes = org.springframework.util.StringUtils.commaDelimitedListToSet(sysOauthClient.getScope());
                modelAndView.addObject("clientId", clientId);
                modelAndView.addObject("state", state);
                modelAndView.addObject("scopeList", authorizedScopes);
                modelAndView.addObject("principalName", principal.getName());

                modelAndView.setViewName("consent");
                return modelAndView;
            }
            else {
                throw new OAuthClientException("clientId 不合法");
            }
        }else {
            throw new OAuthClientException("clientId 不合法");
        }
    }

    /**
     * 检查Token
     * @param token 令牌
     */
    @SneakyThrows
    @GetMapping("/checkToken")
    public void checkToken(String token, HttpServletResponse response, HttpServletRequest request) {
        ServletServerHttpResponse httpResponse = new ServletServerHttpResponse(response);

        if (StrUtil.isBlank(token)) {
            httpResponse.setStatusCode(HttpStatus.UNAUTHORIZED);
            //调用认证失败处理
            this.authenticationFailureHandler.onAuthenticationFailure(request, response,
                    new InvalidBearerTokenException(OAuth2ErrorCodesExpand.TOKEN_MISSING));
            return;
        }
        OAuth2Authorization authorization = authorizationService.findByToken(token, OAuth2TokenType.ACCESS_TOKEN);

        // 如果令牌不存在 返回401
        if (authorization == null || authorization.getAccessToken() == null) {
            //调用认证失败处理
            this.authenticationFailureHandler.onAuthenticationFailure(request, response,
                    new InvalidBearerTokenException(OAuth2ErrorCodesExpand.INVALID_BEARER_TOKEN));
            return;
        }

        Map<String, Object> claims = authorization.getAccessToken().getClaims();
        OAuth2AccessTokenResponse sendAccessTokenResponse = OAuth2EndpointUtils.sendAccessTokenResponse(authorization,
                claims);
        this.accessTokenHttpResponseConverter.write(sendAccessTokenResponse, MediaType.APPLICATION_JSON, httpResponse);
    }

    /**
     * 退出登录
     *
     * @param authHeader Authorization
     */
    @DeleteMapping("/logout")
    public ResponseResult<Boolean> logout(
            @RequestHeader(value = HttpHeaders.AUTHORIZATION, required = false) String authHeader)
    {
        //如果authHeader为空则说明早就退出成功了,直接返回即可
        if (StringUtils.isBlank(authHeader)) {
            return ResponseResult.build(ResponseType.LOGOUT_SUCCESS);
        }
        //替换掉Token中的Bearer，取出真正的Token
        String tokenValue = authHeader.replace(OAuth2AccessToken.TokenType.BEARER.getValue(), StrUtil.EMPTY).trim();
        return removeToken(tokenValue);
    }

    /**
     * 移除token
     * @param token token
     */
//    @Inner
    @DeleteMapping("/{token}")
    public ResponseResult<Boolean> removeToken(@PathVariable("token") String token) {
        OAuth2Authorization authorization = authorizationService.findByToken(token, OAuth2TokenType.ACCESS_TOKEN);
        if (authorization == null) {
            return ResponseResult.build(ResponseType.LOGOUT_SUCCESS);
        }
        OAuth2Authorization.Token<OAuth2AccessToken> accessToken = authorization.getAccessToken();
        if (accessToken == null || StrUtil.isBlank(accessToken.getToken().getTokenValue())) {
            return ResponseResult.build(ResponseType.LOGOUT_SUCCESS);
        }
        // TODO: 2023/5/9 清空用户信息
//        cacheManager.getCache(CacheConstants.USER_DETAILS).evict(authorization.getPrincipalName());
        // 清空accessToken
        authorizationService.remove(authorization);
        // 处理自定义退出事件，保存相关日志
        SpringContextHolder.publishEvent(new LogoutSuccessEvent(new PreAuthenticatedAuthenticationToken(
                authorization.getPrincipalName(), authorization.getRegisteredClientId())));
        return ResponseResult.build(ResponseType.LOGOUT_SUCCESS);
    }


}
