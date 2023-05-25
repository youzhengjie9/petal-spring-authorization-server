package com.petal.oauth2.common.security.utils;

import cn.hutool.core.map.MapUtil;
import com.petal.oauth2.common.base.constant.Oauth2Constant;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.core.OAuth2AuthenticatedPrincipal;

public class SecurityUtil {

    /**
     * 获取客户端id
     *
     * @return clientId
     */
    private String getClientId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null) {
            return null;
        }

        Object principal = authentication.getPrincipal();
        if (principal instanceof OAuth2AuthenticatedPrincipal) {
            OAuth2AuthenticatedPrincipal auth2Authentication = (OAuth2AuthenticatedPrincipal) principal;
            return MapUtil.getStr(auth2Authentication.getAttributes(), Oauth2Constant.CLIENT_ID);
        }
        return null;
    }

    /**
     * 获取用户名称
     *
     * @return username
     */
    private String getUsername() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null) {
            return null;
        }
        return authentication.getName();
    }

}
