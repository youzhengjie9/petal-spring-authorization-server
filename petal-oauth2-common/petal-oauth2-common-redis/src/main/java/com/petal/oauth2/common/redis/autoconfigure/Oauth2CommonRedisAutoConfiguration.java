package com.petal.oauth2.common.redis.autoconfigure;

import com.petal.oauth2.common.redis.config.RedisTemplateConfig;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 自动配置类
 *
 * @author youzhengjie
 * @date 2023/05/10 09:31:09
 */
@Configuration
public class Oauth2CommonRedisAutoConfiguration {

    @Bean
    public RedisTemplateConfig redisTemplateConfig(){

        return new RedisTemplateConfig();
    }


}
