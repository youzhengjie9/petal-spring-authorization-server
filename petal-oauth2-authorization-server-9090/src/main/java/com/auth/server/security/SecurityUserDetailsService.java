package com.auth.server.security;

import com.auth.server.entity.User;
import com.auth.server.mapper.UserMapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

/**
 * SecurityUserDetailsService
 * @author youzhengjie
 * @date 2023/04/22 12:11:23
 */
@Slf4j
@Component
public class SecurityUserDetailsService implements UserDetailsService {

    private UserMapper userMapper;

    @Autowired
    public void setUserMapper(UserMapper userMapper) {
        this.userMapper = userMapper;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        //根据用户名去数据库查询是否有该用户
        User user = userMapper.selectOne(
                new LambdaQueryWrapper<User>().eq(User::getUserName, username)
        );
        //如果用户不存在
        if(Objects.isNull(user)){
            throw new RuntimeException("该用户不存在");
        }
        //模拟该用户权限参数（permission）
        List<SimpleGrantedAuthority> permissions = Arrays.asList(
                new SimpleGrantedAuthority("sys:user:list"),
                new SimpleGrantedAuthority("sys:user:add"),
                new SimpleGrantedAuthority("sys:test3")
        );

        //这里要用org.springframework.security.core.userdetails.User，而不是自定义的UserDetails，不然会报错。
        return new org.springframework.security.core.userdetails.User(
                username,
                user.getPassword(),
                permissions
        );
    }

}