package com.petal.oauth2.resource7000.service;

import com.petal.oauth2.common.base.entity.SysOauth2Client;
import com.baomidou.mybatisplus.extension.service.IService;


public interface SysOauth2ClientService extends IService<SysOauth2Client> {

	/**
	 * 通过ID删除客户端
	 * @param id
	 * @return
	 */
	Boolean removeClientById(String id);

	/**
	 * 修改客户端信息
	 * @param sysOauth2Client
	 * @return
	 */
	Boolean updateClientById(SysOauth2Client sysOauth2Client);

	/**
	 * 清除客户端缓存
	 */
	void clearClientCache();

}
