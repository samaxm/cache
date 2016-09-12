package online.decentworld.cache.redis;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import redis.clients.jedis.Jedis;

import static online.decentworld.cache.redis.CacheKey.WEALTH;


public class WealthCache extends RedisTemplate {

	private static Logger logger=LoggerFactory.getLogger(WealthCache.class);

	
	public int getWealth(String dwID){
		ReturnResult result=cache((Jedis jedis)->{
			String value=jedis.hget(WEALTH,dwID);
			if(value==null){
				return ReturnResult.FAIL;
			}else{
				return ReturnResult.result(Integer.parseInt(value));
			}
		});
		if(result.isSuccess()){
			return (Integer)result.getResult();
		}else{
			logger.debug("[GET_WEALTH_FAILED] dwID#"+dwID);
			return -1;
		}
	}
	
}
