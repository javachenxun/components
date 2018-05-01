package com.chenxun.components.redis;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

/** 
 * @author：chenxun
 * 创建时间：2016年10月16日 下午3:06:53 
 * 参考：
 * 说明：
 * download ：https://github.com/ServiceStack/redis-windows
 */
public class JedisClient {
	
   static Logger LOG = LoggerFactory.getLogger(JedisClient.class);
   
   private static JedisPool pool = null;
   
   private final static String       IP = "127.0.0.1";
   private final static int          PORT  = 6379;
   private final static int          TIMEOUT  = 1000;//单位毫秒
   private final static int          MAXWAITMILLIS  = 1000;//单位毫秒
   private final static int          MAXTOTAL  = 1000;
   
   private JedisClient() {}

    /**
     * 获取连接池
     */
     static {
        try {
            JedisPoolConfig config = new JedisPoolConfig();
            config.setMaxTotal(MAXTOTAL);
            config.setMaxWaitMillis(MAXWAITMILLIS);
            config.setTestOnBorrow(true);
            config.setTestOnReturn(true);

            pool = new JedisPool(config, IP, PORT, TIMEOUT);
            LOG.info("[static ]  init jedisPool success");
        } catch (Exception e) {
            throw  new RuntimeException("获取 JedisPool 异常",e);
        }
    }

    public static Jedis getJedis() {
        return pool.getResource();
    }
}
