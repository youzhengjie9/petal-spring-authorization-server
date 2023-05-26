package com.petal.oauth2.common.security.service;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.SpringSecurityCoreVersion;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.oauth2.core.OAuth2AuthenticatedPrincipal;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;


public class SecurityOauth2User extends User implements OAuth2AuthenticatedPrincipal {

	private static final long serialVersionUID = SpringSecurityCoreVersion.SERIAL_VERSION_UID;

	private final Map<String, Object> attributes = new HashMap<>();

	/**
	 * 用户ID
	 */
	@Getter
	@JsonSerialize(using = ToStringSerializer.class)
	private final Long id;

	/**
	 * 手机号
	 */
	@Getter
	private final String phone;

	public SecurityOauth2User(Long id, String username, String password, String phone, boolean enabled,
							  boolean accountNonExpired, boolean credentialsNonExpired, boolean accountNonLocked,
							  Collection<? extends GrantedAuthority> authorities) {
		super(username, password, enabled, accountNonExpired, credentialsNonExpired, accountNonLocked, authorities);
		this.id = id;
		this.phone = phone;
	}
//	public SecurityOauth2User(Long id, String username, String password, String phone, boolean enabled,
//							  boolean accountNonExpired, boolean credentialsNonExpired, boolean accountNonLocked,
//							  Collection<? extends GrantedAuthority> authorities,Map<String, Object> attributes) {
//		super(username, password, enabled, accountNonExpired, credentialsNonExpired, accountNonLocked, authorities);
//		this.id = id;
//		this.phone = phone;
//		this.attributes=attributes;
//	}

	/**
	 * Get the OAuth 2.0 token attributes
	 * @return the OAuth 2.0 token attributes
	 */
	@Override
	public Map<String, Object> getAttributes() {
		return this.attributes;
	}

	@Override
	public String getName() {
		return this.getUsername();
	}

}


//public class SecurityOauth2User implements UserDetails, OAuth2AuthenticatedPrincipal, Serializable {
//
//	private static final long serialVersionUID = SpringSecurityCoreVersion.SERIAL_VERSION_UID;
//
//	private final Map<String, Object> attributes = new HashMap<>();
//
//	private final SysUser sysUser;
//
//	/**
//	 * 框架所需要的权限集合
//	 */
//	@JSONField(serialize = false) //禁止序列化该属性
//	private final Collection<GrantedAuthority> authorities;
//
//	public SecurityOauth2User(SysUser sysUser,Collection<GrantedAuthority> authorities){
//		this.sysUser=sysUser;
//		this.authorities = authorities;
//	}
//
//	@Override
//	public Collection<? extends GrantedAuthority> getAuthorities() {
//
//		return authorities;
//	}
//
//	@Override
//	public Map<String, Object> getAttributes() {
//		return this.attributes;
//	}
//
//	/**
//	 * 获取用户名
//	 *
//	 * @return {@link String}
//	 */
//	@Override
//	public String getName() {
//		return this.getUsername();
//	}
//
//	@Override
//	public String getPassword() {
//		return sysUser.getPassword();
//	}
//
//	public SysUser getSysUser() {
//		return sysUser;
//	}
//
//	@Override
//	public String getUsername() {
//		return sysUser.getUserName();
//	}
//
//	@Override
//	public boolean isAccountNonExpired() {
//		return true;
//	}
//
//	@Override
//	public boolean isAccountNonLocked() {
//		return true;
//	}
//
//	@Override
//	public boolean isCredentialsNonExpired() {
//		return true;
//	}
//
//	@Override
//	public boolean isEnabled() {
//
//		return (sysUser.getDelFlag()==0 && sysUser.getStatus()==0);
//	}
//}