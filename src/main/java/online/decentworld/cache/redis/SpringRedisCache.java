package online.decentworld.cache.redis;

import com.alibaba.fastjson.JSON;
import online.decentworld.cache.config.CacheConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.Cache;
import org.springframework.cache.support.SimpleValueWrapper;
import redis.clients.jedis.Jedis;

import java.util.concurrent.Callable;

/**
 * 注意，此类仅接受String:RESULTBEAN类型的转换
 * 
 * @author Sammax
 *
 */
public class SpringRedisCache extends RedisTemplate implements Cache {

	private String prefix;
	private Class c;

	private static Logger logger= LoggerFactory.getLogger(SpringRedisCache.class);

	public SpringRedisCache(String name){
		this.prefix=name+ CacheKey.SEPARATOR;
		try {
			c=Class.forName(name);
		} catch (ClassNotFoundException e) {
			c=null;
		}
	}
	
	
	@Override
	public String getName() {
		return prefix.substring(0,prefix.length()-1);
	}

	@Override
	public Object getNativeCache() {
		return null;
	}

	@Override
	public ValueWrapper get(Object key) {
		if(c==null){
			return null;
		}
		ReturnResult result=cache((Jedis jedis)->{
			logger.debug("[GET_CACHE] key#"+key);
			String value=jedis.get(prefix+key);
			return ReturnResult.result(value);
		});
		if(result.isSuccess()&&result.getResult()!=null){
			return new SimpleValueWrapper(JSON.parseObject((String)result.getResult(),c));
		}else{
			return null;	
		}
		
	}

	@Override
	public <T> T get(Object key, Class<T> type) {
		logger.debug("[GET_CACHE] null");
		return null;
	}

	@Override
	public void put(Object key, Object value) {
		cache((Jedis jedis) -> {
			logger.debug("[CACHE] key#"+key+" value#"+value);
			jedis.setex(prefix + key, CacheConfig.CACHE_EXPIRE, JSON.toJSONString(value));
			return ReturnResult.SUCCESS;
		});
	}

	@Override
	public ValueWrapper putIfAbsent(Object key, Object value) {
		ReturnResult result=cache((Jedis jedis)->{
			logger.debug("[CACHE] key#"+key+" value#"+value);
			if(!jedis.exists(prefix+key.toString()))
				jedis.setex(prefix+key,CacheConfig.CACHE_EXPIRE,JSON.toJSONString(value));
			return ReturnResult.SUCCESS;
		});
		if(result.isSuccess()){
			return new ResultBeanValueWraper(value.toString());
		}else{
			return null;
		}
	}

	@Override
	public void evict(Object key) {
		logger.debug("[CACHE_DEL] key#"+key);
		cache((Jedis jedis) -> {
			jedis.del(prefix + key);
			return ReturnResult.SUCCESS;
		});		
	}

	@Override
	public void clear() {
		logger.debug("[CACHE] CLEAR");
		return;
	}


	public <T> T get(Object key, Callable<T> valueLoader) {
		// TODO Auto-generated method stub
		logger.debug("[GET_CACHE] NULL");
		return null;
	}



}
