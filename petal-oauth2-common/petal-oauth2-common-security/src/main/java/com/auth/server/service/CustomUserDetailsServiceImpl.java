package com.auth.server.service;

import com.auth.server.dto.UserInfo;
import com.auth.server.entity.SysUser;
import com.auth.server.feign.SysUserFeign;
import com.auth.server.utils.ResponseResult;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@Primary
@RequiredArgsConstructor
public class CustomUserDetailsServiceImpl implements CustomUserDetailsService {

	private SysUserFeign sysUserFeign;

	@Autowired
	public void setSysUserFeign(SysUserFeign sysUserFeign) {
		this.sysUserFeign = sysUserFeign;
	}

	/**
	 * 用户名密码登录
	 * @param username 用户名
	 * @return
	 */
	@Override
	@SneakyThrows
	public UserDetails loadUserByUsername(String username) {

		SysUser sysUser = sysUserFeign.queryUserByUserName(username);

		UserInfo userInfo = new UserInfo();
		userInfo.setSysUser(sysUser);
		// TODO: 2023/5/6 模拟权限
		userInfo.setPermissions(new String[]{"sys:test3","sys:test5"});

		ResponseResult<UserInfo> result = ResponseResult.ok(userInfo);
		UserDetails userDetails = getUserDetails(result);

		return userDetails;
	}

	@Override
	public int getOrder() {
		return Integer.MIN_VALUE;
	}

}
