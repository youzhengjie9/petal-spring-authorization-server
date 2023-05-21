package com.petal.oauth2.auth.alipay.mapper;

import com.petal.oauth2.auth.alipay.entity.AlipayUser;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

@Mapper
@Repository
public interface AlipayUserMapper extends BaseMapper<AlipayUser> {


}
