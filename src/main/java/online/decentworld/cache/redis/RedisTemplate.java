package online.decentworld.cache.redis;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import redis.clients.jedis.Jedis;

import java.util.*;

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


	public List<String> getFromHSET(String key,List<String> fields,Jedis jedis){
		return getFromHSET(key,toSet(fields),jedis);
	}
	public List<byte[]> getFromHSETBytes(String key,List<String> fields,Jedis jedis){
		return getFromHSETBytes(key,toSet(fields),jedis);
	}

	public List<String> getFromHSET(String key,Set<String> fields,Jedis jedis){
			if(fields==null||fields.size()==0){
				return Collections.emptyList();
			}else if(fields.size()==1){
				List<String> data=new ArrayList<String>(1);
				data.add(jedis.hget(key, fields.iterator().next()));
				return data;
			}else{
				List<String> data=jedis.hmget(key,toStringArr(fields));
				return data;
			}
	}

	public List<byte[]> getFromHSETBytes(String key,Set<String> fields,Jedis jedis){
			if(fields==null||fields.size()==0){
				return Collections.emptyList();
			}else if(fields.size()==1){
				List<byte[]> data=new ArrayList<byte[]>(1);
				data.add(jedis.hget(key.getBytes(), fields.iterator().next().getBytes()));
				return data;
			}else{
				List<byte[]> data=jedis.hmget(key.getBytes(),format(fields));
				return data;
			}
	}

	private Jedis getJedis(){
		return RedisClient.getJedis();
	}
	
	private void releaseJedis(Jedis jedis){
		if(jedis!=null)
			jedis.close();
	}
	private String[] toStringArr(Set<String> key){
		return key.toArray(new String[key.size()]);
	}
	private Set<String> toSet(List<String> list){
		if(list!=null&&list.size()!=0){
			Set<String> set=new HashSet<>();
			list.forEach((String e)->{
				set.add(e);
			});
			return set;
		}else{
			return Collections.EMPTY_SET;
		}
	}
	private byte[][] format(Set<String> ids){
		String[] idArr=ids.toArray(new String[ids.size()]);
		byte[][] bytes=new byte[idArr.length][];
		for(int i=0;i<idArr.length;i++){
			bytes[i]=idArr[i].getBytes();
		}
		return bytes;
	}
}
