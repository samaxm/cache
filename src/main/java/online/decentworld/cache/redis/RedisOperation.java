package online.decentworld.cache.redis;

import redis.clients.jedis.Jedis;

public interface RedisOperation {
	public ReturnResult execute(Jedis jedis);
}
