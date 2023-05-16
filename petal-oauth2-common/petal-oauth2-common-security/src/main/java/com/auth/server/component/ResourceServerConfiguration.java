package com.auth.server.component;

import cn.hutool.core.util.ArrayUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.oauth2.server.resource.introspection.OpaqueTokenIntrospector;
import org.springframework.security.web.SecurityFilterChain;


/**
 * 资源服务器配置
 *
 * @author youzhengjie
 * @date 2023/05/12 18:18:51
 */
@Slf4j
@EnableWebSecurity
@Configuration
public class ResourceServerConfiguration {

	protected CustomAuthenticationEntryPoint customAuthenticationEntryPoint;

	private IgnoreAuthenticationProperties ignoreAuthenticationProperties;

	private Oauth2TokenResolver oauth2TokenResolver;

	private OpaqueTokenIntrospector customOpaqueTokenIntrospector;

	@Autowired
	public void setCustomAuthenticationEntryPoint(CustomAuthenticationEntryPoint customAuthenticationEntryPoint) {
		this.customAuthenticationEntryPoint = customAuthenticationEntryPoint;
	}

	@Autowired
	public void setIgnoreAuthenticationProperties(IgnoreAuthenticationProperties ignoreAuthenticationProperties) {
		this.ignoreAuthenticationProperties = ignoreAuthenticationProperties;
	}

	@Autowired
	public void setOauth2TokenResolver(Oauth2TokenResolver oauth2TokenResolver) {
		this.oauth2TokenResolver = oauth2TokenResolver;
	}

	@Autowired
	public void setCustomOpaqueTokenIntrospector(OpaqueTokenIntrospector customOpaqueTokenIntrospector) {
		this.customOpaqueTokenIntrospector = customOpaqueTokenIntrospector;
	}

	@Bean
	@Order(Ordered.HIGHEST_PRECEDENCE)
	SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

		http.authorizeRequests(authorizeRequests -> authorizeRequests
			// ignoreAuthenticationProperties.getIgnoreUrls()里面的路径不需要认证,允许所有人访问
			.antMatchers(ArrayUtil.toArray(ignoreAuthenticationProperties.getIgnoreUrls(), String.class))
			.permitAll()
			// 除了ignoreAuthenticationProperties.getIgnoreUrls()里面的路径,其余的路径全部都要认证才能访问。
			.anyRequest()
			.authenticated())
			// 说明该模块是资源服务器,和老版本的 @EnableResourceServer 效果一样，只不过 @EnableResourceServer 已经被弃用了
			.oauth2ResourceServer(
					oauth2 -> oauth2.opaqueToken(token -> token.introspector(customOpaqueTokenIntrospector))
							// 配置认证失败处理器
						.authenticationEntryPoint(customAuthenticationEntryPoint)
							// 配置oauth2的Token的解析器
						.bearerTokenResolver(oauth2TokenResolver)
			)
			// 关闭frameOptions
			.headers()
			.frameOptions()
			.disable()
			.and()
			// 关闭csrf
			.csrf()
			.disable();

		return http.build();
	}

}
