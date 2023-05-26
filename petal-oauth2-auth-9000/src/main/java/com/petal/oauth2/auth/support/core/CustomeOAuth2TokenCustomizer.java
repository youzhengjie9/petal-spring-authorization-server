package com.petal.oauth2.auth.support.core;

import com.petal.oauth2.common.base.constant.Oauth2Constant;
import com.petal.oauth2.common.security.service.SecurityOauth2User;
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

	private static final String LICENSE = "http://petal.oauth2.com:9000";

	@Override
	public void customize(OAuth2TokenClaimsContext context) {

		OAuth2TokenClaimsSet.Builder claims = context.getClaims();
		claims.claim(Oauth2Constant.DETAILS_LICENSE, LICENSE);
		String clientId = context.getAuthorizationGrant().getName();
		claims.claim(Oauth2Constant.CLIENT_ID, clientId);
		// 客户端模式不返回具体用户信息
		if (Oauth2Constant.CLIENT_CREDENTIALS.equals(context.getAuthorizationGrantType().getValue())) {
			return;
		}
		SecurityOauth2User securityOauth2User = (SecurityOauth2User) context.getPrincipal().getPrincipal();

//		/*
//			解决,当使用token访问接口时SecurityOauth2User调用super构造时报错: Cannot pass null or empty values to constructor
//			原因是:
//			org.springframework.security.core.userdetails.User的构造方法有个password!=null的条件,
//			而我们存入redis的token信息中password=null,当我们使用token时,security就会去redis中查询这个token的帐号密码等信息,
//			当发现password为null时则会报错,所以我们不能让password=null,可以让password='想看密码?没门'。
//		 */
//
//		securityOauth2User= new SecurityOauth2User(
//				securityOauth2User.getId(),
//				securityOauth2User.getUsername(),
//				"想看密码?没门",
//				securityOauth2User.getPhone(),
//				true,
//				true,
//				true,
//				true,
//				securityOauth2User.getAuthorities(),
//				securityOauth2User.getAttributes()
//		);

		claims.claim(Oauth2Constant.USER_INFO, securityOauth2User);
	}

}
