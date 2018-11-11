package com.core.components.redis;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import redis.clients.jedis.Jedis;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by chenxun on 2017/5/14 16:59
 * 多实例多线程测试
 */
public class ManyInstanceManyThread3 {

    private Logger LOG = LoggerFactory.getLogger(ManyInstanceManyThread3.class);

    private final  String SHARE_KEY = "infoId";
    private final  String SHARE_KEY_L = "infoId_List";
    private final  String SHARE_VALUE = "msg";
    private  final  AtomicInteger doCount = new AtomicInteger();

    class Instance implements Runnable{
        @Override
        public void run() {
            for (int i = 0; i < 100; i++) {
                final int innerI = i;
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        Jedis jedis = JedisClient.getJedis();
                        try {TimeUnit.SECONDS.sleep(1);} catch (InterruptedException e) {}
                        long expires = System.currentTimeMillis() + 1000 + 1;
                        String expiresStr = String.valueOf(expires);
                        boolean lock = jedis.setnx(SHARE_KEY, SHARE_VALUE)==1?true:false;
                        if(lock){
                            jedis.expire(SHARE_KEY,60*10);//单位秒  方式nosqldb崩溃 锁一直占有
                            atomOp(innerI);
                            String value=null;
                            try {
                                while ((value = jedis.lpop(SHARE_KEY_L))!=null){
                                    atomOp(Integer.parseInt(value));
                                }
                            }finally {
                                jedis.del(SHARE_KEY);//方便间隔很大的消息在来时可以获取锁
                            }
                        }else{
                            jedis.rpush(SHARE_KEY_L,innerI+"");
                        }
                        jedis.close();
                    }
                }).start();
            }
        }
        private void atomOp(int innerI) {
            //同步代码
            LOG.info("[atomOp] doCount={}",doCount.incrementAndGet());
        }
    }

    @Test
    public  void batchTest() throws InterruptedException {
        for (int i = 0; i < 10; i++) {
            new Thread(new Instance()).start();
            new Thread(new Instance()).start();
            while (Thread.activeCount()<5){
                Thread.yield();
            }
            while (Thread.activeCount()!=3){
                Thread.yield();
            }
        }
    }
}
