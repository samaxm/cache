import online.decentworld.cache.config.CacheBeanConfig;
import online.decentworld.cache.redis.RedisIDUtil;
import online.decentworld.rdb.config.DBConfig;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * Created by Sammax on 2016/9/12.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes={CacheBeanConfig.class, DBConfig.class})
public class CacheTest {

    @Autowired
    private RedisIDUtil idUtil;
    @Test
    public void test(){
        System.out.println(idUtil.getID(null));
    }
}
