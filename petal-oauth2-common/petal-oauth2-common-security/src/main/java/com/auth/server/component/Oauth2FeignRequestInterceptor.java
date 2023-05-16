package com.auth.server.component;

import cn.hutool.core.collection.CollUtil;
import com.auth.server.constant.Oauth2Constant;
import com.auth.server.utils.WebUtil;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import feign.RequestInterceptor;
import feign.RequestTemplate;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.security.oauth2.core.OAuth2AccessToken;
import org.springframework.security.oauth2.server.resource.web.BearerTokenResolver;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.util.Collection;


/**
 * oauth2的feign请求拦截器
 *
 * @author youzhengjie
 * @date 2023/05/12 15:14:59
 */
@Slf4j
@Component
public class Oauth2FeignRequestInterceptor implements RequestInterceptor {

	private final BearerTokenResolver tokenResolver;

	public Oauth2FeignRequestInterceptor(BearerTokenResolver tokenResolver) {
		this.tokenResolver = tokenResolver;
	}

	/**
	 * 当feign请求发送之前将会进入这个方法,然后我们就把accessToken放到这个feign请求的请求头的Authorization中,然后才放行
	 * @param template
	 */
	@Override
	public void apply(RequestTemplate template) {
		// 获取请求中名为only_feign_call的请求头
		Collection<String> onlyFeignCallHeaderList = template.headers().get(Oauth2Constant.ONLY_FEIGN_CALL_HEADER_NAME);
		// 如果onlyFeignCallHeaderList集合不为空，直接放行
		if (CollUtil.isNotEmpty(onlyFeignCallHeaderList)) {
			return;
		}

		// 不是web请求直接放行
		if (!WebUtil.getRequest().isPresent()) {
			return;
		}
		HttpServletRequest request = WebUtil.getRequest().get();
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
