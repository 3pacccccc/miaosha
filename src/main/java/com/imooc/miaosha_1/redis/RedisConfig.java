package com.imooc.miaosha_1.redis;


import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "redis")
@Data
public class RedisConfig {

    private String host;

    private Integer port;

    private Integer timeout;

    private Integer poolMaxTotal;

    private Integer poolMaxIdle;

    private Integer poolMaxWait;

}
