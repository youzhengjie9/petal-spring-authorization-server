package com.auth.server.support.password;

import com.auth.server.support.OAuth2ResourceOwnerBaseAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.AuthorizationGrantType;

import java.util.Map;
import java.util.Set;


/**
 * 自定义密码授权token的信息
 *
 * @author youzhengjie
 * @date 2023/05/08 13:12:14
 */
public class CustomOAuth2PasswordAuthenticationToken extends OAuth2ResourceOwnerBaseAuthenticationToken {

	public CustomOAuth2PasswordAuthenticationToken(AuthorizationGrantType authorizationGrantType,
												   Authentication clientPrincipal, Set<String> scopes, Map<String, Object> additionalParameters) {
		super(authorizationGrantType, clientPrincipal, scopes, additionalParameters);
	}

}
