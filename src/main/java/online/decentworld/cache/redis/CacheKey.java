package online.decentworld.cache.redis;

/**
 * 緩存鍵管理
 * @author Sammax
 *
 */
public class CacheKey {

	public static String SEPARATOR=":";

	/**
	 * 全部等待用户，field为用户dwID，value为用户的等待队列索引
	 */
	public static String WAITING_TABLE="WAITING:TABLE";
	
	/**
	 * 被举报次数记录,HSET,field为用户ID,value为被举报次数
	 */
	public static String REPORT_TABLE="REPORT:TABLE";
	
	/**
	 * 用户身家缓存,HSET,field为用户ID,value为用户身家
	 */
	public static String WEALTH="WEALTH";

	/**
	 * 用户AES的key缓存，hest,field为用户ID,value为用户key
	 */
	public static String AES="AES";
	/**
	 * CHAT
	 */
	public static String MESSAGE="MESSAGE";
	/**
	 * CHAT ID
	 */
	public static String CHAT_ID="ID:CHAT";
	/**
	 * READ & EXPIRE MESSAGE WAITING STORE SET
	 */
	public static String MESSSAGE_STORE_SET="MESSAGE:STORE";
	/**
	 * USER SESSION INFO
	 */
	public static String SESSION="SESSION";
	/**
	 * CURRENT ONLINE NUMBER
	 */
	public static String ONLINE_NUM="ONLINE";
	/**
	 * HISTORY MAX ONLINE NUMBER
	 */
	public static String MAX_ONLINE="MAX_ONLINE";
	/**
	 * FAIL STORE MESSAGE LIST
	 */
	public static String FAIL_STROE_MSG="FAIL_SOTRE_MSG";

	public static String CONN_DOMAIN="CONN_DOMAIN";
	/**
	 * USER AES KEY, EXPIRE 3 DAYS
	 */
	public static String AESKey(String dwID){
		return AES+SEPARATOR+dwID;
	}


}
