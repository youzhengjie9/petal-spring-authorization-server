package com.petal.oauth2.auth.support.core;

import com.petal.oauth2.auth.support.handler.CustomFormAuthenticationFailureHandler;
import com.petal.oauth2.auth.support.handler.CustomSsoLogoutSuccessHandler;
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
			// 登录页路径
			formLogin.loginPage("/token/login");
			// 登录请求提交到的表单的路径
			formLogin.loginProcessingUrl("/token/form");
			// 表单登录失败处理器
			formLogin.failureHandler(new CustomFormAuthenticationFailureHandler());

		})
			.logout()
			 // Sso单点退出成功处理
			.logoutSuccessHandler(new CustomSsoLogoutSuccessHandler())
			 // 删除cookies
			.deleteCookies("JSESSIONID")
			.invalidateHttpSession(true)
			.and()
			//关闭csrf
			.csrf()
			.disable();
	}

}
