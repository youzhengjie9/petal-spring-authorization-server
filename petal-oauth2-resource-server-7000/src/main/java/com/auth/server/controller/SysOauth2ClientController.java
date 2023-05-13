package com.auth.server.controller;

import com.auth.server.entity.SysOauthClient;
import com.auth.server.service.SysOauthClientService;
import com.auth.server.utils.ResponseResult;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/client")
public class SysOauth2ClientController {

	private final SysOauthClientService sysOauthClientService;

	/**
	 * 通过id查询
	 * @param clientId 客户端id
	 * @return sysOauthClient
	 */
	@GetMapping("/{clientId}")
	public ResponseResult<List<SysOauthClient>> getByClientId(@PathVariable String clientId) {
		return ResponseResult.ok(sysOauthClientService
			.list(Wrappers.<SysOauthClient>lambdaQuery().eq(SysOauthClient::getClientId, clientId)));
	}

	/**
	 * 简单分页查询
	 * @param page 分页对象
	 * @param sysOauthClient 系统终端
	 * @return
	 */
	@GetMapping("/page")
	public ResponseResult<IPage<SysOauthClient>> getOauthClientPage(Page page,
															  SysOauthClient sysOauthClient) {
		return ResponseResult.ok(sysOauthClientService.page(page, Wrappers.query(sysOauthClient)));
	}

	/**
	 * 添加
	 * @param sysOauthClient 实体
	 * @return success/false
	 */
	@PostMapping
	@PreAuthorize("@pms.hasPermission('sys_client_add')")
	public ResponseResult<Boolean> add(@Valid @RequestBody SysOauthClient sysOauthClient) {
		return ResponseResult.ok(sysOauthClientService.save(sysOauthClient));
	}

	/**
	 * 删除
	 * @param id ID
	 * @return success/false
	 */
	@DeleteMapping("/{id}")
	@PreAuthorize("@pms.hasPermission('sys_client_del')")
	public ResponseResult<Boolean> removeById(@PathVariable String id) {
		return ResponseResult.ok(sysOauthClientService.removeClientById(id));
	}

	/**
	 * 编辑
	 * @param sysOauthClient 实体
	 * @return success/false
	 */
	@PutMapping
	@PreAuthorize("@pms.hasPermission('sys_client_edit')")
	public ResponseResult<Boolean> update(@Valid @RequestBody SysOauthClient sysOauthClient) {
		return ResponseResult.ok(sysOauthClientService.updateClientById(sysOauthClient));
	}

	@DeleteMapping("/cache")
	@PreAuthorize("@pms.hasPermission('sys_client_del')")
	public ResponseResult clearClientCache() {
		sysOauthClientService.clearClientCache();
		return ResponseResult.ok();
	}

//	@Inner
	@GetMapping("/getClientById/{clientId}")
	public ResponseResult getClientById(@PathVariable String clientId) {
		return ResponseResult.ok(sysOauthClientService.getOne(
				Wrappers.<SysOauthClient>lambdaQuery().eq(SysOauthClient::getClientId, clientId), false));
	}

}
