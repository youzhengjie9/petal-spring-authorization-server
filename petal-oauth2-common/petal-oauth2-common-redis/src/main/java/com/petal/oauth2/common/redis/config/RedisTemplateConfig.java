package com.petal.oauth2.common.redis.config;

import com.petal.oauth2.common.redis.config.FastJson2RedisSerializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.*;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

/**
 * SpringDataRedis+FastJson2配置类
 *
 * @author youzhengjie
 * @date 2023-05-09 00:28:51
 */
@Configuration
public class RedisTemplateConfig {

    /**
     * redistemplate<String,Object>的bean
     *
     * @param connectionFactory 连接工厂
     * @return {@link RedisTemplate}<{@link String}, {@link Object}>
     */
    @Bean
    @SuppressWarnings(value = { "unchecked", "rawtypes" })
    public RedisTemplate<Object, Object> redisTemplate(RedisConnectionFactory connectionFactory)
    {
        // 创建RedisTemplate<String,Object>对象
        RedisTemplate<Object, Object> template = new RedisTemplate<>();

        // 设置连接工厂
        template.setConnectionFactory(connectionFactory);

        // fastjson2序列化工具
        FastJson2RedisSerializer serializer = new FastJson2RedisSerializer(Object.class);

        // 序列化String数据类型的key和value
        template.setKeySerializer(new StringRedisSerializer());
        template.setValueSerializer(serializer);

        // 序列化Hash数据类型的key和value
        template.setHashKeySerializer(new StringRedisSerializer());
        template.setHashValueSerializer(serializer);

        template.afterPropertiesSet();
        return template;
    }


//    @Bean
//    public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory factory) {
//        RedisTemplate<String, Object> redisTemplate = new RedisTemplate<>();
//        redisTemplate.setKeySerializer(RedisSerializer.string());
//        redisTemplate.setHashKeySerializer(RedisSerializer.string());
//        redisTemplate.setValueSerializer(RedisSerializer.java());
//        redisTemplate.setHashValueSerializer(RedisSerializer.java());
//        redisTemplate.setConnectionFactory(factory);
//        return redisTemplate;
//    }
//
//    @Bean
//    public HashOperations<String, String, Object> hashOperations(RedisTemplate<String, Object> redisTemplate) {
//        return redisTemplate.opsForHash();
//    }
//
//    @Bean
//    public ValueOperations<String, String> valueOperations(RedisTemplate<String, String> redisTemplate) {
//        return redisTemplate.opsForValue();
//    }
//
//    @Bean
//    public ListOperations<String, Object> listOperations(RedisTemplate<String, Object> redisTemplate) {
//        return redisTemplate.opsForList();
//    }
//
//    @Bean
//    public SetOperations<String, Object> setOperations(RedisTemplate<String, Object> redisTemplate) {
//        return redisTemplate.opsForSet();
//    }
//
//    @Bean
//    public ZSetOperations<String, Object> zSetOperations(RedisTemplate<String, Object> redisTemplate) {
//        return redisTemplate.opsForZSet();
//    }
}
