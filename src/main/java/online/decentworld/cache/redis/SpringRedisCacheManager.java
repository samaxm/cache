package online.decentworld.cache.redis;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;

import java.util.Collection;


public class SpringRedisCacheManager implements CacheManager{

	private static Logger logger= LoggerFactory.getLogger(SpringRedisCacheManager.class);

	@Override
	public Cache getCache(String name) {
		return new SpringRedisCache(name);
	}

	@Override
	public Collection<String> getCacheNames() {
		return null;
	}



}
