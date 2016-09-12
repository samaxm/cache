package online.decentworld.cache.redis;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import redis.clients.jedis.Jedis;

/**
 * 
 * @author Sammax
 *
 */

public class RedisTemplate {

	private static Logger logger=LoggerFactory.getLogger(RedisTemplate.class);
	
	
	public ReturnResult cache(RedisOperation operation){
		Jedis jedis=null;
		try{
			jedis=getJedis();
			return operation.execute(jedis);
		}catch(Exception ex){
			logger.warn("[REDIS_OPERATION_FAILED]",ex);
			return ReturnResult.FAIL;
		}finally{
			releaseJedis(jedis);
		}
	}
	
	private Jedis getJedis(){
		return RedisClient.getJedis();
	}
	
	private void releaseJedis(Jedis jedis){
		if(jedis!=null)
			jedis.close();
	}
	
}
