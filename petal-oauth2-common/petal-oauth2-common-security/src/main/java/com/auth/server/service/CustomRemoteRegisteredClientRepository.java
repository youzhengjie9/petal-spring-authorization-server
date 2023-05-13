package com.auth.server.service;

import cn.hutool.core.util.BooleanUtil;
import cn.hutool.core.util.StrUtil;
import com.auth.server.entity.SysOauthClient;
import com.auth.server.feign.SysOauth2ClientFeign;
import com.auth.server.utils.ResponseResult;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.ClientAuthenticationMethod;
import org.springframework.security.oauth2.core.OAuth2Error;
import org.springframework.security.oauth2.server.authorization.authentication.OAuth2AuthorizationCodeRequestAuthenticationException;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClientRepository;
import org.springframework.security.oauth2.server.authorization.settings.ClientSettings;
import org.springframework.security.oauth2.server.authorization.settings.OAuth2TokenFormat;
import org.springframework.security.oauth2.server.authorization.settings.TokenSettings;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.time.Duration;
import java.util.Arrays;
import java.util.Optional;


/**
 * 查询客户端相关信息实现
 * 作用: 登录请求中会携带 Basic base64(clientId:clientSecret),
 * 那么首先OAuth2ClientAuthenticationFilter会通过调用RegisteredClientRepository类去数据库查询传入的客户端是否正确
 *
 * @author youzhengjie
 * @date 2023/05/10 10:37:37
 */
@Component
@Primary
public class CustomRemoteRegisteredClientRepository implements RegisteredClientRepository {

	/**
	 * 刷新令牌有效期默认 30 天
	 */
	private final static int refreshTokenValiditySeconds = 60 * 60 * 24 * 30;

	/**
	 * 请求令牌有效期默认 12 小时
	 */
	private final static int accessTokenValiditySeconds = 60 * 60 * 12;

	private SysOauth2ClientFeign sysOauth2ClientFeign;

	@Autowired
	public void setSysOauth2ClientFeign(SysOauth2ClientFeign sysOauth2ClientFeign) {
		this.sysOauth2ClientFeign = sysOauth2ClientFeign;
	}

	/**
	 * 保存RegisteredClient
	 */
	@Override
	public void save(RegisteredClient registeredClient) {
		throw new UnsupportedOperationException();
	}


	@Override
	public RegisteredClient findById(String id) {
		throw new UnsupportedOperationException();
	}

	/**
	 * 重写原生方法支持redis缓存
	 * @param clientId
	 * @return
	 */
	@Override
	@SneakyThrows
	public RegisteredClient findByClientId(String clientId) {

		ResponseResult responseResult = sysOauth2ClientFeign.getClientById(clientId);
		if(responseResult != null){

			Object data = responseResult.getData();

			if(data == null){
				throw new OAuth2AuthorizationCodeRequestAuthenticationException
						(new OAuth2Error("客户端查询异常，请检查数据库链接"), null);
			}else {
				SysOauthClient sysOauthClient = (SysOauthClient) data;
				RegisteredClient.Builder builder = RegisteredClient.withId(sysOauthClient.getClientId())
						.clientId(sysOauthClient.getClientId())
						.clientSecret("noop" + sysOauthClient.getClientSecret())
						.clientAuthenticationMethod(ClientAuthenticationMethod.CLIENT_SECRET_POST)
						.clientAuthenticationMethods(clientAuthenticationMethods -> {
							clientAuthenticationMethods.add(ClientAuthenticationMethod.CLIENT_SECRET_BASIC);
							clientAuthenticationMethods.add(ClientAuthenticationMethod.CLIENT_SECRET_POST);
						});

				// 授权模式
				Optional.ofNullable(sysOauthClient.getAuthorizedGrantTypes())
						.ifPresent(grants -> StringUtils.commaDelimitedListToSet(grants)
								.forEach(s -> builder.authorizationGrantType(new AuthorizationGrantType(s))));
				// 回调地址
				Optional.ofNullable(sysOauthClient.getWebServerRedirectUri())
						.ifPresent(redirectUri -> Arrays.stream(redirectUri.split(StrUtil.COMMA))
								.filter(StrUtil::isNotBlank)
								.forEach(builder::redirectUri));

				// scope
				Optional.ofNullable(sysOauthClient.getScope())
						.ifPresent(scope -> Arrays.stream(scope.split(StrUtil.COMMA))
								.filter(StrUtil::isNotBlank)
								.forEach(builder::scope));

				return builder
						.tokenSettings(TokenSettings.builder()
								.accessTokenFormat(OAuth2TokenFormat.REFERENCE)
								.accessTokenTimeToLive(Duration.ofSeconds(
										Optional.ofNullable(sysOauthClient.getAccessTokenValidity()).orElse(accessTokenValiditySeconds)))
								.refreshTokenTimeToLive(Duration.ofSeconds(Optional.ofNullable(sysOauthClient.getRefreshTokenValidity())
										.orElse(refreshTokenValiditySeconds)))
								.build())
						.clientSettings(ClientSettings.builder()
								.requireAuthorizationConsent(!BooleanUtil.toBoolean(sysOauthClient.getAutoapprove()))
								.build())
						.build();
			}
		}else {
			throw new OAuth2AuthorizationCodeRequestAuthenticationException
					(new OAuth2Error("客户端查询异常，请检查数据库链接"), null);
		}

	}

}
