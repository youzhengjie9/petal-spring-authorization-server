package com.petal.oauth2.common.security.service;

import com.petal.oauth2.common.base.constant.Oauth2Constant;
import com.petal.oauth2.common.base.entity.SysUser;
import com.petal.oauth2.common.base.utils.ResponseResult;
import com.petal.oauth2.common.openfeign.feign.SysUserFeign;
import com.petal.oauth2.common.security.dto.UserInfo;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;

/**
 * 手机验证码模式下的UserDetailsService实现类
 *
 * @author youzhengjie
 * @date 2023/05/13 00:23:44
 */
@Slf4j
@RequiredArgsConstructor
public class SmsUserDetailsServiceImpl implements CustomUserDetailsService {

	private final SysUserFeign sysUserFeign;

	/**
	 * 手机号登录
	 * @param phone 手机号
	 * @return
	 */
	@Override
	@SneakyThrows
	public UserDetails loadUserByUsername(String phone) {

		ResponseResult<SysUser> sysUserResponseResult = sysUserFeign.queryUserByPhone(phone);

		UserInfo userInfo = new UserInfo();
		userInfo.setSysUser(sysUserResponseResult.getData());
		// TODO: 2023/5/14 模拟权限
		userInfo.setPermissions(new String[]{"sys:test3"});

		return getUserDetails(ResponseResult.ok(userInfo));
	}


	@Override
	public UserDetails loadUserBySecurityOauth2User(SecurityOauth2User securityOauth2User) {
		return this.loadUserByUsername(securityOauth2User.getPhone());
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