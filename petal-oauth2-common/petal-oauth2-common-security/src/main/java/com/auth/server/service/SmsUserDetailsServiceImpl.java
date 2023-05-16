package com.auth.server.service;

import com.auth.server.constant.Oauth2Constant;
import com.auth.server.dto.UserInfo;
import com.auth.server.entity.SysUser;
import com.auth.server.feign.SysUserFeign;
import com.auth.server.utils.ResponseResult;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

/**
 * 手机验证码模式下的UserDetailsService实现类
 *
 * @author youzhengjie
 * @date 2023/05/13 00:23:44
 */
@Slf4j
@Component
public class SmsUserDetailsServiceImpl implements CustomUserDetailsService {

	private SysUserFeign sysUserFeign;

	@Autowired
	public void setSysUserFeign(SysUserFeign sysUserFeign) {
		this.sysUserFeign = sysUserFeign;
	}

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

		ResponseResult<UserInfo> userInfoResponseResult = ResponseResult.ok(userInfo);

		return getUserDetails(userInfoResponseResult);
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
