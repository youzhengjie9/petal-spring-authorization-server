package com.auth.server.filter;

import com.auth.server.constant.Oauth2Constant;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.util.StringUtils;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.Arrays;
import java.util.Collections;
import java.util.stream.Collectors;

import static org.springframework.cloud.gateway.support.ServerWebExchangeUtils.GATEWAY_REQUEST_URL_ATTR;
import static org.springframework.cloud.gateway.support.ServerWebExchangeUtils.addOriginalRequestUrl;

/**
 * @author lengleng
 * @date 2019/2/1
 * <p>
 * 全局拦截器，作用所有的微服务
 * <p>
 * 1. 对请求头中参数进行处理 from 参数进行清洗 2. 重写StripPrefix = 1,支持全局
 * <p>
 * 支持swagger添加X-Forwarded-Prefix header （F SR2 已经支持，不需要自己维护）
 */
public class RequestGlobalFilter implements GlobalFilter, Ordered {

	/**
	 * Process the Web request and (optionally) delegate to the next {@code WebFilter}
	 * through the given {@link GatewayFilterChain}.
	 * @param exchange the current server exchange
	 * @param chain provides a way to delegate to the next filter
	 * @return {@code Mono<Void>} to indicate when request processing is complete
	 */
	@Override
	public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
		// 把从网关进来的请求的请求头（only_feign_call）去除。
		// 因为我们要控制@PermitAll注解的onlyFeignCall属性设置为true的接口只能通过openfeign所访问。
		// 如果在浏览器通过访问gateway的方式访问该接口则会访问失败.（原理就是在网关把请求的请求头（only_feign_call）去除即可）
		ServerHttpRequest request = exchange.getRequest().mutate().headers(httpHeaders -> {
			httpHeaders.remove(Oauth2Constant.ONLY_FEIGN_CALL_HEADER_NAME);
			// 记录当前请求的开始时间
			httpHeaders.put(Oauth2Constant.REQUEST_START_TIME,
					Collections.singletonList(String.valueOf(System.currentTimeMillis())));
		}).build();

		// >>>>> 代码实现StripPrefix = 1同样的功能 (这样我们就不需要在application.yml配置filters为StripPrefix = 1了)
		// （例如访问http://网关ip:网关端口/petal-oauth2-auth-9000/bbb,实际上访问的是http://网关ip:网关端口/bbb ）
		addOriginalRequestUrl(exchange, request.getURI());
		String rawPath = request.getURI().getRawPath();
		String newPath = "/" + Arrays.stream(StringUtils.tokenizeToStringArray(rawPath, "/"))
				// StripPrefix = 1
			.skip(1L)
			.collect(Collectors.joining("/"));

		ServerHttpRequest newRequest = request.mutate().path(newPath).build();
		exchange.getAttributes().put(GATEWAY_REQUEST_URL_ATTR, newRequest.getURI());

		return chain.filter(exchange.mutate().request(newRequest.mutate().build()).build());
	}

	@Override
	public int getOrder() {
		return 10;
	}

}
