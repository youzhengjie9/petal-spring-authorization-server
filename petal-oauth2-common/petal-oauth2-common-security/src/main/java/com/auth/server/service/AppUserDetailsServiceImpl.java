package com.auth.server.service;

import com.auth.server.constant.Oauth2Constant;
import com.auth.server.dto.UserInfo;
import com.auth.server.feign.SysUserFeign;
import com.auth.server.utils.ResponseResult;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;

/**
 * 应用程序用户详细信息服务impl
 *
 * @author youzhengjie
 * @date 2023/05/13 00:23:44
 */
@Slf4j
@RequiredArgsConstructor
public class AppUserDetailsServiceImpl implements CustomUserDetailsService {

	private final SysUserFeign sysUserFeign;

	/**
	 * 手机号登录
	 * @param phone 手机号
	 * @return
	 */
	@Override
	@SneakyThrows
	public UserDetails loadUserByUsername(String phone) {

		ResponseResult<UserInfo> result = sysUserFeign.infoByMobile(phone);

		UserDetails userDetails = getUserDetails(result);

		return userDetails;
	}


	@Override
	public UserDetails loadUserBySecurityOauth2User(SecurityOauth2User securityOauth2User) {
		return this.loadUserByUsername(SecurityOauth2User.getPhone());
	}

	/**
	 * 是否支持此客户端校验
	 * @param clientId 目标客户端
	 * @return true/false
	 */
	@Override
	public boolean support(String clientId, String grantType) {
		return Oauth2Constant.SMS_GRANT_TYPE.equals(grantType);
	}

}
