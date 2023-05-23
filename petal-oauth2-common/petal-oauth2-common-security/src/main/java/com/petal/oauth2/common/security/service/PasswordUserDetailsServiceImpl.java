package com.petal.oauth2.common.security.service;

import com.petal.oauth2.common.base.entity.SysUser;
import com.petal.oauth2.common.base.utils.ResponseResult;
import com.petal.oauth2.common.openfeign.feign.SysUserFeign;
import com.petal.oauth2.common.security.dto.UserInfo;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Primary;
import org.springframework.security.core.userdetails.UserDetails;

/**
 * 帐号密码模式下的UserDetailsService实现类
 *
 * @author youzhengjie
 * @date 2023/05/13 23:49:00
 */
@Slf4j
@Primary
@RequiredArgsConstructor
public class PasswordUserDetailsServiceImpl implements CustomUserDetailsService {

	private final SysUserFeign sysUserFeign;

	/**
	 * 用户名密码登录
	 * @param username 用户名
	 * @return
	 */
	@Override
	@SneakyThrows
	public UserDetails loadUserByUsername(String username) {

		ResponseResult<SysUser> sysUserResponseResult = sysUserFeign.queryUserByUserName(username,"123");

		UserInfo userInfo = new UserInfo();
		userInfo.setSysUser(sysUserResponseResult.getData());
		// TODO: 2023/5/6 模拟权限
		userInfo.setPermissions(new String[]{"sys:test3","sys:test5"});

		return getUserDetails(ResponseResult.ok(userInfo));
	}

	@Override
	public int getOrder() {
		return Integer.MIN_VALUE;
	}

}
