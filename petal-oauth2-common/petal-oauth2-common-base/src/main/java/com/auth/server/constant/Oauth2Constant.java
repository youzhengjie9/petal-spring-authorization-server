package com.auth.server.constant;

import cn.hutool.core.codec.Base64;

public final class Oauth2Constant {


	/**
	 * 手机验证码登录授权类型
	 */
	public static final String SMS_GRANT_TYPE = "sms:login";

	/**
	 * 帐号密码登录参数名称（username）
	 */
	public static final String USERNAME_PARAMETER_NAME = "username";
	/**
	 * 帐号密码登录参数名称（password）
	 */
	public static final String PASSWORD_PARAMETER_NAME = "password";

	/**
	 * 手机验证码登录参数名称（sms）
	 */
	public static final String SMS_PARAMETER_NAME = "sms";

	/**
	 * 用户信息
	 */
    public static final String USER_INFO = "user_info";


	/**
	 * 协议字段
	 */
	public static final String DETAILS_LICENSE = "license";

	/**
	 * 客户端id
	 */
	public static final String CLIENT_ID = "clientId";


	/**
	 * 客户端模式
	 */
	public static final String CLIENT_CREDENTIALS = "client_credentials";

	/**
	 * 标记只能被feign调用的请求头的名称
	 */
	public static final String ONLY_FEIGN_CALL_HEADER_NAME = "only_feign_call";

}
