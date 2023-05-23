package com.petal.oauth2.common.security.component;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.server.resource.BearerTokenError;
import org.springframework.security.oauth2.server.resource.BearerTokenErrors;
import org.springframework.security.oauth2.server.resource.web.BearerTokenResolver;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.PathMatcher;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * oauth2的Token的解析器
 * <p>
 * 作用是: 拿到请求里的accessToken,对accessToken进行校验
 * @author youzhengjie
 * @date 2023/05/11 22:39:45
 */
public class Oauth2TokenResolver implements BearerTokenResolver {

	/**
	 * 校验token用的正则表达式
	 */
	private static final Pattern authorizationPattern = Pattern.compile("^Bearer (?<token>[a-zA-Z0-9-:._~+/]+=*)$",
			Pattern.CASE_INSENSITIVE);

	private boolean allowFormEncodedBodyParameter = false;

	private boolean allowUriQueryParameter = true;

	private String bearerTokenHeaderName = HttpHeaders.AUTHORIZATION;

	private final PathMatcher pathMatcher = new AntPathMatcher();

	private final IgnoreAuthenticationProperties ignoreAuthenticationProperties;

	public Oauth2TokenResolver(IgnoreAuthenticationProperties ignoreAuthenticationProperties) {
		this.ignoreAuthenticationProperties = ignoreAuthenticationProperties;
	}

	/**
	 * 解析request
	 *
	 * @param request 请求
	 * @return {@link String} 返回解析出来的token
	 */
	@Override
	public String resolve(HttpServletRequest request) {
		//当前请求的uri
		String currentRequestURI = request.getRequestURI();
		//判断当前请求的uri是否在ignoreUrls集合中（也就是该请求的uri是否可以不需要鉴权就可以被访问）
		boolean match = ignoreAuthenticationProperties.getIgnoreUrls()
			.stream()
			.anyMatch(ignoreUrl -> pathMatcher.match(ignoreUrl,currentRequestURI));

		//如果匹配成功直接return跳出该方法，不进行后面的token校验
		if (match) {
			return null;
		}
		//解析token
		final String authorizationHeaderToken = resolveFromAuthorizationHeader(request);
		final String parameterToken = isParameterTokenSupportedForRequest(request)
				? resolveFromRequestParameters(request) : null;
		if (authorizationHeaderToken != null) {
			if (parameterToken != null) {
				final BearerTokenError error = BearerTokenErrors
					.invalidRequest("Found multiple bearer tokens in the request");
				throw new OAuth2AuthenticationException(error);
			}
			return authorizationHeaderToken;
		}
		if (parameterToken != null && isParameterTokenEnabledForRequest(request)) {
			return parameterToken;
		}
		return null;
	}

	private String resolveFromAuthorizationHeader(HttpServletRequest request) {
		String authorization = request.getHeader(this.bearerTokenHeaderName);
		//如果请求头上的Authorization的token没有以bearer开头则直接返回null，校验失败
		if (!StringUtils.startsWithIgnoreCase(authorization, "bearer")) {
			return null;
		}
		//校验token
		Matcher matcher = authorizationPattern.matcher(authorization);
		//如果校验token失败，直接抛出异常
		if (!matcher.matches()) {
			BearerTokenError error = BearerTokenErrors.invalidToken("Bearer token is malformed");
			throw new OAuth2AuthenticationException(error);
		}
		//校验token成功
		return matcher.group("token");
	}

	private static String resolveFromRequestParameters(HttpServletRequest request) {
		String[] values = request.getParameterValues("access_token");
		if (values == null || values.length == 0) {
			return null;
		}
		if (values.length == 1) {
			return values[0];
		}
		BearerTokenError error = BearerTokenErrors.invalidRequest("Found multiple bearer tokens in the request");
		throw new OAuth2AuthenticationException(error);
	}

	private boolean isParameterTokenSupportedForRequest(final HttpServletRequest request) {
		return (("POST".equals(request.getMethod())
				&& MediaType.APPLICATION_FORM_URLENCODED_VALUE.equals(request.getContentType()))
				|| "GET".equals(request.getMethod()));
	}

	private boolean isParameterTokenEnabledForRequest(final HttpServletRequest request) {
		return ((this.allowFormEncodedBodyParameter && "POST".equals(request.getMethod())
				&& MediaType.APPLICATION_FORM_URLENCODED_VALUE.equals(request.getContentType()))
				|| (this.allowUriQueryParameter && "GET".equals(request.getMethod())));
	}

}
