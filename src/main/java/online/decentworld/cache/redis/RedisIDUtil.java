package online.decentworld.cache.redis;

import online.decentworld.cache.config.CacheConfig;
import online.decentworld.rdb.mapper.IDMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import redis.clients.jedis.Jedis;

import javax.annotation.PostConstruct;

/**
 * Created by Sammax on 2016/9/12.
 */
public class RedisIDUtil extends RedisTemplate {

    private long currentLimint;
    private static Logger logger= LoggerFactory.getLogger(RedisIDUtil.class);

   @Autowired
   private IDMapper mapper;

    @PostConstruct
    public void initID(){
        currentLimint=mapper.getChatID().getChatIdRoof();
        logger.debug("Current ID#"+currentLimint);
    }

    public long getID(Jedis outjedis){
        if(outjedis!=null){
            ReturnResult result= cache((Jedis jedis)->{
                long id=jedis.incr(CacheKey.CHAT_ID);
                if(id==1){
                    //if cache is empty,restore the roof
                    jedis.set(CacheKey.CHAT_ID, String.valueOf(id));
                }
                if(id==currentLimint){
                    try {
                        mapper.updateChatID(CacheConfig.ID_INCREASEMENT);
                        currentLimint+=CacheConfig.ID_INCREASEMENT;
                    }catch (Exception e){
                        logger.warn("[INCREASE_ID_FAILED]",e);
                    }
                }
                return ReturnResult.result(id);
            });
            return (long) result.getResult();
        }else{
            long id=outjedis.incr(CacheKey.CHAT_ID);
            if(id==1){
                //if cache is empty,restore the roof
                outjedis.set(CacheKey.CHAT_ID, String.valueOf(currentLimint));
                id=currentLimint;
                return id;
            }
            if(id==currentLimint){
                try {
                    mapper.updateChatID(CacheConfig.ID_INCREASEMENT);
                    currentLimint+=CacheConfig.ID_INCREASEMENT;
                }catch (Exception e){
                    logger.warn("[INCREASE_ID_FAILED]",e);
                }
            }
            return id;
        }
    }

}
