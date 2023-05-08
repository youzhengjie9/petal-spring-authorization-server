package com.auth.server.mapper;

import com.auth.server.entity.AlipayUser;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

@Mapper
@Repository
public interface AlipayUserMapper extends BaseMapper<AlipayUser> {


}
