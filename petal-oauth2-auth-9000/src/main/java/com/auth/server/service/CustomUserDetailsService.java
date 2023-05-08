package com.auth.server.service;

import com.auth.server.constant.SecurityConstants;
import com.auth.server.dto.UserInfo;
import com.auth.server.entity.SysUser;
import com.auth.server.utils.ResponseResult;
import org.springframework.core.Ordered;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.*;


/**
 * 自定义UserDetailsService
 *
 * @author youzhengjie
 * @date 2023/05/08 11:28:32
 */
public interface CustomUserDetailsService extends UserDetailsService, Ordered {

	/**
	 * 是否支持此客户端校验
	 * @param clientId 目标客户端
	 * @return true/false
	 */
	default boolean support(String clientId, String grantType) {
		return true;
	}

	/**
	 * 排序值 默认取最大的
	 * @return 排序值
	 */
	default int getOrder() {
		return 0;
	}

	/**
	 * 构建userdetails
	 * @param result 用户信息
	 * @return UserDetails
	 */
	default UserDetails getUserDetails(ResponseResult<UserInfo> result) {

		UserInfo info ;

		if(Objects.isNull(result) || Objects.isNull(result.getData())){
			throw new UsernameNotFoundException("用户不存在");
		}
		info = result.getData();

		Set<String> dbAuthsSet = new HashSet<>();

		// TODO: 2023/5/6 暂未实现
//		if (ArrayUtil.isNotEmpty(info.getRoles())) {
//			// 获取角色
//			Arrays.stream(info.getRoles()).forEach(role -> dbAuthsSet.add(SecurityConstants.ROLE + role));
//			// 获取资源
//			dbAuthsSet.addAll(Arrays.asList(info.getPermissions()));
//		}

		// 获取资源
		dbAuthsSet.addAll(Arrays.asList(info.getPermissions()));

		Collection<GrantedAuthority> authorities = AuthorityUtils
			.createAuthorityList(dbAuthsSet.toArray(new String[0]));
		SysUser user = info.getSysUser();

		// 构造SecurityOauth2User用户
		return new SecurityOauth2User(user.getId(), user.getUserName(),
				SecurityConstants.BCRYPT + user.getPassword(), user.getPhone(), true, true, true,
				true, authorities);
	}

	/**
	 * 通过SecurityOauth2User查询UserDetails
	 * @param securityOauth2User SecurityOauth2User用户
	 * @return
	 */
	default UserDetails loadUserBySecurityOauth2User(SecurityOauth2User securityOauth2User) {
		return this.loadUserByUsername(securityOauth2User.getUsername());
	}

}
