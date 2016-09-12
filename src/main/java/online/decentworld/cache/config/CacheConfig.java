package online.decentworld.cache.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileInputStream;
import java.util.Properties;

/**
 * Created by Sammax on 2016/9/12.
 */
public class CacheConfig {
    public static int CACHE_EXPIRE;
    public static int ID_INCREASEMENT;
    private static String CONFIG_FILE="cache.properties";
    private static Logger logger= LoggerFactory.getLogger(CacheConfig.class);

    static{
        Properties cacheConfig=new Properties();
        try {
            String path=CacheConfig.class.getClassLoader().getResource(CONFIG_FILE).getPath();
            logger.debug("[CONFIG_FILE_PATH] path#"+path);
            cacheConfig.load(new FileInputStream(path));
            for(String property:cacheConfig.stringPropertyNames()){
                CacheConfig.class.getField(property).set(null, Integer.valueOf(cacheConfig.getProperty(property)));
            }
        } catch (Exception e) {
            logger.error("[LOAD_CONFIG_FAILED]",e);
        }
    }

    public static void main(String[] args) {
        System.out.println(CacheConfig.CACHE_EXPIRE);
    }
}
