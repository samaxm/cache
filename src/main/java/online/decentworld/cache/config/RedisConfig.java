package online.decentworld.cache.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Field;
import java.util.Properties;

/**
 * Created by Sammax on 2016/9/12.
 */
public class RedisConfig {
    public static String CODIS_ZK_CONNECTSTR;
    public static String CODIS_PROXY_NAMESPACE;
    public static String CONFIG_FILE="codis_config.properties";
    private static Logger logger= LoggerFactory.getLogger(RedisConfig.class);

    static{
        Properties codisPro=new Properties();
        try {
            codisPro.load(RedisConfig.class.getClassLoader().getResourceAsStream(CONFIG_FILE));
            for(String property:codisPro.stringPropertyNames()){
                RedisConfig.class.getField(property).set(null, codisPro.getProperty(property));
            }
            checkNull(RedisConfig.class);
        } catch (Exception e) {
            logger.error("[LOAD_CONFIG_FAILED]",e);
        }
    }


    private static void checkNull(Class<?> clazz){
        for(Field field:clazz.getDeclaredFields()){
            try {
                Object o=field.get(null);
                if(o==null){
                    logger.warn("[NULL_PROPERTIES] property#"+field.getName());
                    throw new RuntimeException();
                }
            } catch (IllegalArgumentException | IllegalAccessException e) {
                logger.warn("[INIT_PROPERTIES_ERROR]",e);
            }
        }
    }


}
