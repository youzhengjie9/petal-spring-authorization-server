package com.auth.server.config;

import org.springframework.cloud.gateway.filter.ratelimit.KeyResolver;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import reactor.core.publisher.Mono;

import java.util.Objects;


/**
 * 路由限流配置
 *
 * @author youzhengjie
 * @date 2023/05/14 22:47:47
 */
@Configuration(proxyBeanMethods = false)
public class RouteRateLimiterConfig {

	/**
	 * 作用: 把用户的IP作为限流的key进行限流
	 *
	 * @return {@link KeyResolver}
	 */
	@Bean
	public KeyResolver ipAddressKeyResolver() {
		return exchange -> Mono
			.just(Objects.requireNonNull(Objects.requireNonNull(exchange.getRequest().getRemoteAddress()))
				.getAddress()
				.getHostAddress());
	}

}
