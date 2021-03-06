package online.decentworld.cache.redis;

import io.codis.jodis.JedisResourcePool;
import io.codis.jodis.RoundRobinJedisPool;
import online.decentworld.tools.Environment;
import online.decentworld.tools.EnvironmentCofing;
import redis.clients.jedis.*;

import java.util.ArrayList;
import java.util.List;

import static online.decentworld.cache.config.RedisConfig.*;

public class RedisClient {
	
	
	private static JedisResourcePool jodisPool;
    private static JedisPool jedisPool;//非切片连接池
    private static ShardedJedisPool shardedJedisPool;//切片连接池
    static{
    	initialPool(); 
        initialShardedPool();  
    }

    public static Jedis getJedis(){
    	if(EnvironmentCofing.environment== Environment.LOCAL){
            return jedisPool.getResource();
        }else {
            return jodisPool.getResource();
        }
    }

    
    public static ShardedJedis getShardedJedis(){
    	ShardedJedis shardedJedis=shardedJedisPool.getResource();
    	return shardedJedis;
    }
    
  
    /**
     * 初始化非切片池
     */
    private static void initialPool() 
    { 
    	
        JedisPoolConfig config = new JedisPoolConfig(); 
        config.setMaxTotal(200);
        config.setMaxIdle(50);
        config.setMaxWaitMillis(10000);

        if(EnvironmentCofing.environment== Environment.LOCAL){
            jedisPool=new JedisPool(config,"112.74.13.117",6379,100000,"decentworld2015");
        }else{
            jodisPool=RoundRobinJedisPool.create().curatorClient(CODIS_ZK_CONNECTSTR, 30000).zkProxyDir(CODIS_PROXY_NAMESPACE).poolConfig(config).build();
        }
    }
            
    /** 
     * 初始化切片池 
     */ 
    private static void initialShardedPool() 
    { 
        // 池基本配置 
        JedisPoolConfig config = new JedisPoolConfig(); 
        config.setMaxTotal(20); 
        config.setMaxIdle(5); 
        config.setMaxWaitMillis(1000l); 
        config.setTestOnBorrow(false); 
        // slave链接 
        List<JedisShardInfo> shards = new ArrayList<JedisShardInfo>(); 
        shards.add(new JedisShardInfo("112.74.13.117", 6379, "master")); 

        // 构造池 
        shardedJedisPool = new ShardedJedisPool(config, shards); 
    } 

    
    public static void main(String[] args) {
    	Jedis jedis=getJedis();
    	System.out.println(jedis.get("aaa"));
    	jedis.setex("bbb",100,"test");
    	System.out.println(jedis.get("bbb"));
    }
}