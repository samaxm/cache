package online.decentworld.cache.config;

import online.decentworld.cache.redis.RedisIDUtil;
import online.decentworld.cache.redis.SpringRedisCacheManager;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * Created by Sammax on 2016/9/12.
 */
@Configuration
@EnableCaching
@ComponentScan(basePackages={"online.decentworld.cache.*"})
public class CacheBeanConfig {

    @Bean
    public CacheManager getCacheManager(){
        return new SpringRedisCacheManager();
    }

    @Bean
    public RedisIDUtil getRedisIDUtil(){
        return new RedisIDUtil();
    }
}
