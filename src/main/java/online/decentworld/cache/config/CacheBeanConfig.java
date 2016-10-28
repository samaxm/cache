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


    @Bean
    public CacheManager redisCacheManager() {
        logger.debug("[INIT_REDIS_CACHE]");
        return new SpringRedisCacheManager();
    }
    @Bean
    public CacheManager cacheManager() {
        logger.debug("[INIT_LOCAL_CACHE]");
        return new ConcurrentMapCacheManager();
    }

    @Bean(name = "default_cache_resolver")
    public CacheResolver getCacheResolver(){
        logger.debug("[GET_RESOLVER]");
        CacheManager redisCacheManager=new SpringRedisCacheManager();
        CacheManager localCacheManager=new ConcurrentMapCacheManager();
        return new CacheResolver() {
            @Override
            public Collection<? extends Cache> resolveCaches(CacheOperationInvocationContext<?> context) {
                String cacheName=context.getOperation().getCacheNames().iterator().next();
                logger.debug("[RESOLVE_CACHE] cacheName#"+cacheName);
                List<Cache> caches = new ArrayList<Cache>();
                if(cacheName.startsWith("redis")){
                    String name=cacheName.split("_")[1];
                    if(name!=null){
                        caches.add(redisCacheManager.getCache(name));
                        return caches;
                    }else{
                        return null;
                    }
                }else if(cacheName.startsWith("local")){
                    caches.add(localCacheManager.getCache(cacheName));
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
