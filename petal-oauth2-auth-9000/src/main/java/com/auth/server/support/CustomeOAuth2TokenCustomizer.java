package com.auth.server.support;

import com.auth.server.constant.SecurityConstants;
import com.auth.server.service.SecurityOauth2User;
import org.springframework.security.oauth2.server.authorization.token.OAuth2TokenClaimsContext;
import org.springframework.security.oauth2.server.authorization.token.OAuth2TokenClaimsSet;
import org.springframework.security.oauth2.server.authorization.token.OAuth2TokenCustomizer;


/**
 * 增强Oauth2的Token
 *
 * @author youzhengjie
 * @date 2023/05/08 12:08:46
 */
public class CustomeOAuth2TokenCustomizer implements OAuth2TokenCustomizer<OAuth2TokenClaimsContext> {

	/**
	 * Customize the OAuth 2.0 Token attributes.
	 * @param context the context containing the OAuth 2.0 Token attributes
	 */
	@Override
	public void customize(OAuth2TokenClaimsContext context) {
		OAuth2TokenClaimsSet.Builder claims = context.getClaims();
		claims.claim(SecurityConstants.DETAILS_LICENSE, "http://petal.oauth2.com:9000");
		String clientId = context.getAuthorizationGrant().getName();
		claims.claim(SecurityConstants.CLIENT_ID, clientId);
		// 客户端模式不返回具体用户信息
		if (SecurityConstants.CLIENT_CREDENTIALS.equals(context.getAuthorizationGrantType().getValue())) {
			return;
		}
		SecurityOauth2User securityOauth2User = (SecurityOauth2User) context.getPrincipal().getPrincipal();
		claims.claim(SecurityConstants.DETAILS_USER, securityOauth2User);
	}

}
