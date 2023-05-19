package com.petal.oauth2.resource7000.service.impl;

import com.petal.oauth2.common.base.entity.SysOauth2Client;
import com.petal.oauth2.resource7000.mapper.SysOauth2ClientMapper;
import com.petal.oauth2.resource7000.service.SysOauth2ClientService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

@Service
public class SysOauth2ClientServiceImpl extends ServiceImpl<SysOauth2ClientMapper, SysOauth2Client> implements SysOauth2ClientService {


    @Override
    public Boolean removeClientById(String id) {
        return this.removeById(id);
    }

    @Override
    public Boolean updateClientById(SysOauth2Client sysOauth2Client) {
        return this.updateById(sysOauth2Client);
    }

    @Override
    public void clearClientCache() {
        System.out.println("clearClientCache...");
    }
}
