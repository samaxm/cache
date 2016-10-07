package online.decentworld.cache.config;

import online.decentworld.cache.redis.RedisIDUtil;
import online.decentworld.cache.redis.SpringRedisCacheManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CachingConfigurerSupport;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.concurrent.ConcurrentMapCacheManager;
import org.springframework.cache.interceptor.CacheOperationInvocationContext;
import org.springframework.cache.interceptor.CacheResolver;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Created by Sammax on 2016/9/12.
 */
@Configuration
@EnableCaching
@ComponentScan(basePackages={"online.decentworld.cache.*"})
public class CacheBeanConfig extends CachingConfigurerSupport {

    private static Logger logger= LoggerFactory.getLogger(CacheBeanConfig.class);

    private CacheManager redisCache=new SpringRedisCacheManager();

    private CacheManager localCache=new ConcurrentMapCacheManager();
    @Bean
    public CacheManager cacheManager() {
        return null;
    }
    @Bean(name = "default_cache_resolver")
    public CacheResolver getCacheResolver(){
        return new CacheResolver() {
            @Override
            public Collection<? extends Cache> resolveCaches(CacheOperationInvocationContext<?> context) {
                String cacheName=context.getOperation().getCacheNames().iterator().next();
                List<Cache> caches = new ArrayList<Cache>();
                if(cacheName.startsWith("redis")){
                    caches.add(redisCache.getCache(cacheName));
                    return caches;
                }else if(cacheName.startsWith("local")){
                    caches.add(localCache.getCache(cacheName));
                    return caches;
                }else{
                    return null;
                }
            }
        };
    }


    @Bean
    public RedisIDUtil getRedisIDUtil(){
        return new RedisIDUtil();
    }
}
