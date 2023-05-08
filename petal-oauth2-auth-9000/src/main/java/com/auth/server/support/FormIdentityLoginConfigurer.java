package com.auth.server.support;

import com.auth.server.handler.CustomFormAuthenticationFailureHandler;
import com.auth.server.handler.CustomSsoLogoutSuccessHandler;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;


/**
 * 表单个性化登录配置
 *
 * @author youzhengjie
 * @date 2023/05/06 00:35:52
 */
public final class FormIdentityLoginConfigurer
		extends AbstractHttpConfigurer<FormIdentityLoginConfigurer, HttpSecurity> {

	@Override
	public void init(HttpSecurity http) throws Exception {
		http.formLogin(formLogin -> {
			formLogin.loginPage("/token/login");
			formLogin.loginProcessingUrl("/token/form");
			formLogin.failureHandler(new CustomFormAuthenticationFailureHandler());

		})
			.logout()
			 // Sso单点退出成功处理
			.logoutSuccessHandler(new CustomSsoLogoutSuccessHandler())
			.deleteCookies("JSESSIONID")
			.invalidateHttpSession(true)
			.and()
			.csrf()
			.disable();
	}

}
