package com.auth.server.dto;

import com.auth.server.entity.SysUser;
import lombok.Data;
import java.io.Serializable;

@Data
public class UserInfo implements Serializable {

	/**
	 * 用户基本信息
	 */
	private SysUser sysUser;

	/**
	 * 权限标识集合
	 */
	private String[] permissions;

//	/**
//	 * 角色集合
//	 */
//	private Long[] roles;
//
//	/**
//	 * 角色集合
//	 */
//	private List<SysRole> roleList;


}
