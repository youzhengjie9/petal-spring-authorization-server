package com.petal.oauth2.common.security.component;

import com.petal.oauth2.common.security.utils.PermissionService;
import com.fasterxml.jackson.databind.ObjectMapper;
import feign.RequestInterceptor;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationService;
import org.springframework.security.oauth2.server.resource.introspection.OpaqueTokenIntrospector;
import org.springframework.security.oauth2.server.resource.web.BearerTokenResolver;


/**
 * 资源服务器自动配置类
 *
 * @author youzhengjie
 * @date 2023/05/12 15:44:13
 */

@EnableConfigurationProperties(IgnoreAuthenticationProperties.class)
public class ResourceServerAutoConfiguration {

	/**
	 * 配置权限判断工具的bean
	 */
	@Bean("pms")
	public PermissionService permissionService() {
		return new PermissionService();
	}

	/**
	 * oauth2的Token的解析器
	 *
	 * @param ignoreAuthenticationProperties 所有需要忽略权限校验的路径
	 * @return Oauth2TokenResolver
	 */
	@Bean
	public Oauth2TokenResolver oauth2TokenResolver(IgnoreAuthenticationProperties ignoreAuthenticationProperties) {
		return new Oauth2TokenResolver(ignoreAuthenticationProperties);
	}

	/**
	 * 资源服务器异常处理
	 *
	 * @param objectMapper jackson 输出对象
	 * @param securityMessageSource 自定义国际化处理器
	 * @return CustomAuthenticationEntryPoint
	 */
	@Bean
	public CustomAuthenticationEntryPoint customAuthenticationEntryPoint(ObjectMapper objectMapper,
                                                                          MessageSource securityMessageSource) {
		return new CustomAuthenticationEntryPoint(objectMapper, securityMessageSource);
	}

	/**
	 * 自定义token自省器
	 */
	@Bean
	public OpaqueTokenIntrospector opaqueTokenIntrospector(OAuth2AuthorizationService redisOAuth2AuthorizationService) {
		return new CustomOpaqueTokenIntrospector(redisOAuth2AuthorizationService);
	}

	/**
	 * oauth2的feign请求拦截器
	 * @param tokenResolver token获取处理器
	 * @return 拦截器
	 */
	@Bean
	public RequestInterceptor oauth2FeignRequestInterceptor(BearerTokenResolver tokenResolver) {
		return new Oauth2FeignRequestInterceptor(tokenResolver);
	}

}
