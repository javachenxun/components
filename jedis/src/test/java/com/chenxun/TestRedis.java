package com.chenxun;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.Transaction;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.TimeUnit;

/**
 * Created by chenxun on 2017/5/7 18:26
 *
 * 1.redis中5中数据类型：String，List，Set，ZSet，Hash
 * 2.五中类型的key只有String是简单的key-value形式，其他的key相当于集合的名字
 * 3.说明
 *   String:略
 *   List：redis中的队列，主要方法jedis.rpush, jedis.lpush, jedis.rpop, jedis.lpop
 *   Set： jedis.sxxx
 *   ZSet: jedis.zxxx
 *   Hash：jedis.hxxx jedis.hmxxx：多value操作
 *
 */
public class TestRedis {

    private  Logger LOG = LoggerFactory.getLogger(TestRedis.class);
    private Jedis jedis = null;

    @Before
    public void init(){
         jedis = JedisClient.getJedis();
         LOG.info("[init] 获取连接成功 ");
    }
    @After
    public void close(){
        jedis.close();
        LOG.info("[close] 关闭连接 ");
    }

    /**
     * String类型
     * @throws InterruptedException
     */
    @Test
    public  void setAndGet() throws InterruptedException {

        jedis.set("foo", "bar");
        LOG.info("[setAndGet] foo={} exists={}",jedis.get("foo"),jedis.exists("foo"));

        jedis.append("foo", "bar");
        LOG.info("[setAndGet] 字符串追加 foo={} exists={}",jedis.get("foo"),jedis.exists("foo"));

        jedis.expire("foo", 1);//单位秒
        LOG.info("[setAndGet] 设置key为foo过期时间1秒 foo={} exists={}",jedis.get("foo"),jedis.exists("foo"));

        LOG.info("[setAndGet] sleep 2秒");
        TimeUnit.SECONDS.sleep(2);
        LOG.info("[setAndGet] sleep 2秒后获取 foo={} exists={}",jedis.get("foo"),jedis.exists("foo"));

        jedis.set("foo", "bar");
        jedis.del("foo");
        LOG.info("[setAndGet] 添加后删除key为foo  foo={} exists={}",jedis.get("foo"),jedis.exists("foo"));
    }

    /**
     * List类型
     * 主要功能是push、pop
     * 先进后出 ：左边压入队列
     */
    @Test
    public  void firstInLastOut_Left()  {
        //压入栈低
        for (int i = 0; i < 5; i++) {
            jedis.lpush("listkey", "listvalue"+i);
        }
        //栈顶弹出
        Long llen = jedis.llen("listkey");
        for (int i = 0; i < llen; i++) {
            System.out.println(jedis.lpop("listkey"));
        }
    }
    /**
     *  先进后出：右边压入队列
     */
    @Test
    public  void firstInLastOut_Right()  {
        for (int i = 0; i < 5; i++) {
            jedis.rpush("listkey", "message"+i);
        }
        for (int i = 0; i < 5; i++) {
            System.out.println(jedis.rpop("listkey"));
        }
    }
    /**
     *  先进先出：左边入队,右边出队(或右边入队,左边出队)
     */
    @Test
    public  void firstInFirstOut()  {
//        for (int i = 0; i < 5; i++) {
//            jedis.rpush("listkey", "message"+i);
//        }
//        for (int i = 0; i < 5; i++) {
//            System.out.println(jedis.lpop("listkey"));
//        }

        for (int i = 0; i < 5; i++) {
            jedis.lpush("listkey", "message"+i);
        }
        for (int i = 0; i < 5; i++) {
            System.out.println(jedis.rpop("listkey"));
        }
    }




    /**
     * 无序的取出  ，没有的为null
     *  Set类型：jedis.sxxxx  :set的操作  元素添取出无序，不允许有重复元素
     */
    @Test
    public  void set()  {
        jedis.del("setkey");
        for (int i = 0; i < 5; i++) {
            jedis.sadd("setkey", "message"+i);
        }
        for (int i = 0; i < 6; i++) {
            System.out.println(jedis.spop("setkey"));
        }
    }

    /**
     * ZSet(Sorted Set)类型 :Zset是set的一个升级版本，在set的基础上增加了一个顺序属性.
     * jedis.zxxxx
     * 有序存储,取出的顺序为存入的顺序
     */
    @Test
    public  void sortSet()  {
        jedis.del("setkey");
        Random random = new Random();
        for (int i = 0; i < 5; i++) {
            jedis.zadd("setkey", random.nextInt(10) ,"message"+i);//取出的顺序不受中间参数的影响
        }
        System.out.println(jedis.zcard("setkey"));//返回集合的元素个数
        for (int i = 0; i < 5; i++) {
            jedis.zrem("setkey", "message"+i);//删除
            System.out.println(jedis.zrange("setkey",0,-1));
        }
        System.out.println(jedis.zcard("setkey"));
    }

    /**
     * hash类型
     * 类似于java中的HashMap，
     * 并提供了直接存取这个Map成员的接口
     */
    @Test
    public  void hashOp()  {
        Map<String, String> map = new HashMap<String, String>();
        map.put("name", "chenxun");
        map.put("age", "22");
        jedis.hmset("map",map);
        LOG.info("[hashOp] 分别获取map中的多个key的value，返回集合 map={}",jedis.hmget("map", "name","age").toString());

        LOG.info("[hashOp] 获取map中的所有key，keys={}",jedis.hkeys("map").toString());
        LOG.info("[hashOp] 获取map中的所有key的value，values={}",jedis.hvals("map"));
        LOG.info("[hashOp] 获取map中的所有多少个key keysLength={}",jedis.hlen("map"));

        jedis.hdel("map","name");
        LOG.info("[hashOp] 删除map中的name后，values={}",jedis.hvals("map"));

        jedis.del("map");
        LOG.info("[hashOp] 删除key为map后 ，exists={}", jedis.exists("map"));
    }
}
