package com.auth.server.component;

import cn.hutool.core.collection.CollUtil;
import com.auth.server.constant.Oauth2Constant;
import com.auth.server.utils.WebUtils;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import feign.RequestInterceptor;
import feign.RequestTemplate;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.security.oauth2.core.OAuth2AccessToken;
import org.springframework.security.oauth2.server.resource.web.BearerTokenResolver;

import javax.servlet.http.HttpServletRequest;
import java.util.Collection;


/**
 * oauth2的feign请求拦截器
 *
 * @author youzhengjie
 * @date 2023/05/12 15:14:59
 */
@Slf4j
@RequiredArgsConstructor
public class Oauth2FeignRequestInterceptor implements RequestInterceptor {

	private final BearerTokenResolver tokenResolver;

	/**
	 * 当feign请求发送之前将会进入这个方法,然后我们就把accessToken放到这个feign请求的请求头的Authorization中,然后才放行
	 * @param template
	 */
	@Override
	public void apply(RequestTemplate template) {
		Collection<String> fromHeader = template.headers().get(Oauth2Constant.FROM);
		// 带 from 请求直接跳过
		if (CollUtil.isNotEmpty(fromHeader) && fromHeader.contains(Oauth2Constant.FROM_IN)) {
			return;
		}

		// 不是web请求直接放行
		if (!WebUtils.getRequest().isPresent()) {
			return;
		}
		HttpServletRequest request = WebUtils.getRequest().get();
		//解析请求,拿到token
		String token = tokenResolver.resolve(request);
		//如果解析出来的token为空,直接放行
		if (StringUtils.isBlank(token)) {
			return;
		}
		// 拼接出accessToken
		String accessToken = String.format("%s %s", OAuth2AccessToken.TokenType.BEARER.getValue(), token);
		// 在feign请求发送之前,把accessToken放到请求头的Authorization中,只有这样我们这个feign请求才携带了accessToken
		template.header(HttpHeaders.AUTHORIZATION, accessToken);
	}

}
