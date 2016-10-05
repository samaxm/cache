package online.decentworld.cache.redis;

import org.springframework.stereotype.Component;
import redis.clients.jedis.Jedis;

import java.util.Set;

/**
 * Created by Sammax on 2016/9/21.
 */
@Component
public class ApplicationInfoCache extends RedisTemplate {

    private long MAX_ONLINE=0;
    private long MAX_IDLE_TIME=10*60*1000;

    public ApplicationInfoCache(){
        cache((Jedis jedis)->{
            String max=jedis.get(CacheKey.MAX_ONLINE);
            if(max!=null){
                MAX_ONLINE=Long.valueOf(max);
            }else{
                jedis.set(CacheKey.MAX_ONLINE,"0");
            }
            return ReturnResult.SUCCESS;
        });
    }
    public void markOnline(String dwID){
        ReturnResult result=cache((Jedis jedis) -> {
            jedis.zadd(CacheKey.ONLINE_NUM, System.currentTimeMillis(), dwID);
            return ReturnResult.result(jedis.zcard(CacheKey.ONLINE_NUM));
        });
        long size=(long)result.getResult();
        if(size>MAX_ONLINE){
            cache((Jedis jedis) -> {
                String max=jedis.get(CacheKey.MAX_ONLINE);
                if(size>Long.parseLong(max)){
                    jedis.set(CacheKey.MAX_ONLINE,String.valueOf(size));
                }else{
                    MAX_ONLINE=Long.parseLong(max);
                }
                return ReturnResult.SUCCESS;
            });
        }
    }


    public long checkOnline(){
        return (long)cache((Jedis jedis)->{
            Set<String> expireIDs=jedis.zrangeByScore(CacheKey.ONLINE_NUM, 0, System.currentTimeMillis() - MAX_IDLE_TIME);
            //remove expired session info
            if(expireIDs.size()!=0){
                String[] ids=expireIDs.toArray(new String[expireIDs.size()]);
                jedis.zrem(CacheKey.ONLINE_NUM,ids);
                jedis.hdel(CacheKey.SESSION,ids);
            }
            long online=jedis.zcard(CacheKey.ONLINE_NUM);
            return ReturnResult.result(online);
        }).getResult();

    }

    public long getMAX_ONLINE() {
        return MAX_ONLINE;
    }


}
