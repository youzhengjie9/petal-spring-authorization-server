package com.petal.oauth2.alipay.auth.mapper;

import com.petal.oauth2.alipay.auth.entity.AlipayUser;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

@Mapper
@Repository
public interface AlipayUserMapper extends BaseMapper<AlipayUser> {


}
