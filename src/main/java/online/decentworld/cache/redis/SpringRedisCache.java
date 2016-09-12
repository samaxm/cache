package online.decentworld.cache.redis;

import com.alibaba.fastjson.JSON;
import online.decentworld.cache.config.CacheConfig;
import online.decentworld.rpc.dto.api.ResultBean;
import org.springframework.cache.Cache;
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


	public SpringRedisCache(String name){
		this.prefix=name+ CacheKey.SEPARATOR;
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
		ReturnResult result=cache((Jedis jedis)->{
			String value=jedis.get(prefix+key);
			return ReturnResult.result(value);
		});
		if(result.isSuccess()&&result.getResult()!=null){
			return new ResultBeanValueWraper((String)result.getResult());
		}else{
			return null;	
		}
		
	}

	@Override
	public <T> T get(Object key, Class<T> type) {
		return null;
	}

	@Override
	public void put(Object key, Object value) {
		if(!(value instanceof ResultBean)){
			return;
		}
		cache((Jedis jedis) -> {
			jedis.setex(prefix + key, CacheConfig.CACHE_EXPIRE, JSON.toJSONString(value));
			return ReturnResult.SUCCESS;
		});
	}

	@Override
	public ValueWrapper putIfAbsent(Object key, Object value) {
		if(!(value instanceof ResultBean)){
			return null;
		}
		ReturnResult result=cache((Jedis jedis)->{
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
		cache((Jedis jedis) -> {
			jedis.del(prefix + key);
			return ReturnResult.SUCCESS;
		});		
	}

	@Override
	public void clear() {
		return;
	}


	public <T> T get(Object key, Callable<T> valueLoader) {
		// TODO Auto-generated method stub
		return null;
	}



}
