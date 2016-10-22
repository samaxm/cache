package online.decentworld.cache.redis;

import com.alibaba.fastjson.JSON;
import online.decentworld.rdb.entity.BaseDisplayUserInfo;
import online.decentworld.rdb.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import redis.clients.jedis.Jedis;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

/**
 * Created by Sammax on 2016/10/5.
 */
@Component
public class SessionCache extends RedisTemplate {

    private static int AMOUNT_EVERY_PAGE=20;

    @Autowired
    private UserMapper userMapper;

    public void cacheSessionInfo(BaseDisplayUserInfo info){
        cache((Jedis jedis)->{
            jedis.hset(CacheKey.SESSION,info.getDwID(), JSON.toJSONString(info));
            return ReturnResult.SUCCESS;
        });
    }

    public BaseDisplayUserInfo getSessionInfo(String dwID){
        ReturnResult result=cache((Jedis jedis) -> {
            return ReturnResult.result(JSON.parseObject(jedis.hget(CacheKey.SESSION, dwID),BaseDisplayUserInfo.class));
        });
        if(!result.isSuccess()||result.getResult()== null) {
            //can't find in cache , get from db
            BaseDisplayUserInfo info=new BaseDisplayUserInfo(userMapper.selectByPrimaryKey(dwID));
            cacheSessionInfo(info);
            return info;
        } else {
            return (BaseDisplayUserInfo) result.getResult();
        }
    }

    /**
     * display all session info by pages
     * @param page
     * @return
     */
    public List<BaseDisplayUserInfo> getSessionInfos(int page){
        //get online user id
        ReturnResult result= cache((Jedis jedis)->{
            List<BaseDisplayUserInfo> list=new ArrayList<BaseDisplayUserInfo>(AMOUNT_EVERY_PAGE);
            //zrevrange to find latest active user
           Set<String> ids=jedis.zrevrange(CacheKey.ONLINE_NUM, page * AMOUNT_EVERY_PAGE, (page + 1) * AMOUNT_EVERY_PAGE);
            if(ids.size()>1){
                List<String> infos=jedis.hmget(CacheKey.SESSION,ids.toArray(new String[ids.size()]));
                infos.forEach((String info)->{
                    if(info!=null){
                        list.add(JSON.parseObject(info,BaseDisplayUserInfo.class));
                    }
                });
            }else if(ids.size()==1){
                String info=jedis.hget(CacheKey.SESSION,ids.iterator().next());
                if(info!=null)
                list.add(JSON.parseObject(info,BaseDisplayUserInfo.class));
            }
            return ReturnResult.result(list);
        });

        if(result.isSuccess()){
            return (List<BaseDisplayUserInfo>) result.getResult();
        }else{
            return Collections.EMPTY_LIST;
        }
    }


    public boolean cacheUserConnDomain(String dwID,String domain){
        ReturnResult result=cache((Jedis jedis)->{
            Long set=jedis.hsetnx(CacheKey.CONN_DOMAIN,dwID,domain);
            if(set==0){
                return ReturnResult.result(false);
            }else{
                return ReturnResult.result(true);
            }
        });
        return (boolean) result.getResult();
    }
}
