package com.auth.server.autoconfigure;

import com.auth.server.config.DataSourceConfig;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class Oauth2CommonMybatisAutoConfiguration {

    @Bean
    public DataSourceConfig dataSourceConfig(){
        return new DataSourceConfig();
    }

//    @Bean
//    public RemoveDruidAdConfiguration removeDruidAdConfiguration(){
//        return new RemoveDruidAdConfiguration();
//    }


}
