package com.auth.server.controller;

import com.auth.server.annotation.PermitAll;
import com.auth.server.entity.SysOauthClient;
import com.auth.server.service.SysOauthClientService;
import com.auth.server.utils.ResponseResult;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@Api("oauth2-client控制器")
@RestController
@RequiredArgsConstructor
@RequestMapping("/client")
public class SysOauth2ClientController {

	private final SysOauthClientService sysOauthClientService;

	/**
	 * 通过客户端id查询Oauth2客户端信息
	 *
	 * @param clientId 客户端id
	 * @return sysOauthClient
	 */
	@GetMapping("/{clientId}")
	@ApiOperation("通过客户端id查询Oauth2客户端信息")
	public ResponseResult<List<SysOauthClient>> getByClientId(@PathVariable String clientId) {
		return ResponseResult.ok(sysOauthClientService
			.list(Wrappers.<SysOauthClient>lambdaQuery().eq(SysOauthClient::getClientId, clientId)));
	}

	/**
	 * 通过客户端id查询Oauth2客户端信息
	 *
	 * @param clientId 客户机id
	 * @return {@link ResponseResult}
	 */
	@PermitAll
	@GetMapping("/getClientById/{clientId}")
	@ApiOperation("通过客户端id查询Oauth2客户端信息")
	public ResponseResult getClientById(@PathVariable String clientId) {
		return ResponseResult.ok(sysOauthClientService.getOne(
				Wrappers.<SysOauthClient>lambdaQuery().eq(SysOauthClient::getClientId, clientId), false));
	}

	/**
	 * 分页查询Oauth2客户端信息
	 *
	 * @param page 分页对象
	 * @param sysOauthClient 系统终端
	 * @return
	 */
	@GetMapping("/page")
	@ApiOperation("分页查询Oauth2客户端信息")
	public ResponseResult<IPage<SysOauthClient>> getOauthClientPage(Page page,
															  SysOauthClient sysOauthClient) {
		return ResponseResult.ok(sysOauthClientService.page(page, Wrappers.query(sysOauthClient)));
	}

	/**
	 * 添加Oauth2客户端信息
	 *
	 * @param sysOauthClient 实体
	 * @return success/false
	 */
	@PostMapping
	@PreAuthorize("@pms.hasPermission('sys_client_add')")
	@ApiOperation("添加Oauth2客户端信息")
	public ResponseResult<Boolean> add(@Valid @RequestBody SysOauthClient sysOauthClient) {

		return ResponseResult.ok(sysOauthClientService.save(sysOauthClient));
	}

	/**
	 * 删除指定Oauth2客户端id的数据
	 *
	 * @param id 客户端id
	 * @return success/false
	 */
	@DeleteMapping("/{id}")
	@PreAuthorize("@pms.hasPermission('sys_client_del')")
	@ApiOperation("删除指定Oauth2客户端id的数据")
	public ResponseResult<Boolean> removeById(@PathVariable String id) {
		return ResponseResult.ok(sysOauthClientService.removeClientById(id));
	}

	/**
	 * 修改Oauth2客户端信息
	 *
	 * @param sysOauthClient 实体
	 * @return success/false
	 */
	@PutMapping
	@PreAuthorize("@pms.hasPermission('sys_client_edit')")
	@ApiOperation("修改Oauth2客户端信息")
	public ResponseResult<Boolean> update(@Valid @RequestBody SysOauthClient sysOauthClient) {
		return ResponseResult.ok(sysOauthClientService.updateClientById(sysOauthClient));
	}

	/**
	 * 清除客户端缓存
	 *
	 * @return {@link ResponseResult}
	 */
	@DeleteMapping("/cache")
	@PreAuthorize("@pms.hasPermission('sys_client_del')")
	@ApiOperation("清除客户端缓存")
	public ResponseResult clearClientCache() {
		sysOauthClientService.clearClientCache();
		return ResponseResult.ok();
	}


}
