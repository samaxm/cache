package online.decentworld.cache.redis;

import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;

import java.util.Collection;


public class SpringRedisCacheManager implements CacheManager{

	@Override
	public Cache getCache(String name) {
		return new SpringRedisCache(name);
	}

	@Override
	public Collection<String> getCacheNames() {
		return null;
	}



}
