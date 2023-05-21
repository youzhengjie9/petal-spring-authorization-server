package com.petal.oauth2.common.swagger.autoconfigure;

import com.petal.oauth2.common.swagger.config.SwaggerConfig;
import com.petal.oauth2.common.swagger.config.WebMvcConfig;
import com.petal.oauth2.common.swagger.properties.SwaggerProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

/**
 * 自动配置类
 *
 * @author youzhengjie
 * @date 2023-05-15 21:40:26
 */
@Configuration
@EnableConfigurationProperties(SwaggerProperties.class)
@Import({SwaggerConfig.class, WebMvcConfig.class})
public class SwaggerAutoConfiguration {



}







