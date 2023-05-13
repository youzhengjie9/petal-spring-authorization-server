package com.auth.server.service;

import com.auth.server.entity.SysOauthClient;
import com.baomidou.mybatisplus.extension.service.IService;


public interface SysOauthClientService extends IService<SysOauthClient> {

	/**
	 * 通过ID删除客户端
	 * @param id
	 * @return
	 */
	Boolean removeClientById(String id);

	/**
	 * 修改客户端信息
	 * @param sysOauthClient
	 * @return
	 */
	Boolean updateClientById(SysOauthClient sysOauthClient);

	/**
	 * 清除客户端缓存
	 */
	void clearClientCache();

}
