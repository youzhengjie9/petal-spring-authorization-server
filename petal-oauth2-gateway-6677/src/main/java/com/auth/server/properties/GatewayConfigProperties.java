package com.auth.server.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.List;


/**
 * 网关配置属性
 *
 * @author youzhengjie
 * @date 2023/05/17 09:45:30
 */
@Data
//@RefreshScope
@ConfigurationProperties("gateway")
public class GatewayConfigProperties {

	/**
	 * 网关解密登录前端密码秘钥
	 */
	private String encodeKey;

	/**
	 * 网关不需要校验验证码的客户端
	 */
	private List<String> ignoreCheckCaptchaClients;

}
