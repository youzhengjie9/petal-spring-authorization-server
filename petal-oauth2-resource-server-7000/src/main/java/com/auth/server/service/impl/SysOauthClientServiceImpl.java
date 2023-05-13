package com.auth.server.service.impl;

import com.auth.server.entity.SysOauthClient;
import com.auth.server.mapper.SysOauthClientMapper;
import com.auth.server.service.SysOauthClientService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

@Service
public class SysOauthClientServiceImpl extends ServiceImpl<SysOauthClientMapper, SysOauthClient> implements SysOauthClientService {


    @Override
    public Boolean removeClientById(String id) {
        return this.removeById(id);
    }

    @Override
    public Boolean updateClientById(SysOauthClient sysOauthClient) {
        return this.updateById(sysOauthClient);
    }

    @Override
    public void clearClientCache() {
        System.out.println("clearClientCache...");
    }
}
