package com.chenxun.components.redis;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import redis.clients.jedis.Jedis;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by chenxun on 2017/5/14 16:59
 * 多实例多线程测试
 */
public class ManyInstanceManyThread {

    private Logger LOG = LoggerFactory.getLogger(ManyInstanceManyThread.class);
    private Jedis jedis = null;

    private final  Map SHARE_DB = new HashMap(1);
    private final  String SHARE_KEY = "key";
    private final  String SHARE_VALUE = "value";
    private  final AtomicInteger atomicInteger = new AtomicInteger();

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
    class Instance implements Runnable{
        private  Object lock;
        public  Instance( Object l){
            this.lock = l;
        }

        @Override
        public void run() {
            for (int i = 0; i < 1000; i++) {
                final int innerI = i;
                final Object innerLock = this.lock;
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {TimeUnit.SECONDS.sleep(1);} catch (InterruptedException e) {}
                        synchronized (innerLock){
                            if(SHARE_DB.size()==1){
                                SHARE_DB.clear();
                                int i1 = atomicInteger.incrementAndGet();
                                SHARE_DB.put(SHARE_KEY + innerI, SHARE_VALUE + innerI);
                            }else{
                                SHARE_DB.put(SHARE_KEY + innerI, SHARE_VALUE + innerI);
                            }
                        }
                    }
                }).start();
            }
        }
    }

    @Test
    public  void test() throws InterruptedException {
        getCountAndThreadName();
        SHARE_DB.put(SHARE_KEY,SHARE_VALUE);
        Object o = new Object();
        new Thread(new Instance(o)).start();
        new Thread(new Instance(o)).start();

        while (Thread.activeCount()<5){
            Thread.yield();
        }
        while (Thread.activeCount()!=3){
            Thread.yield();
        }

        LOG.info("[test] share_db size={} atomicInteger={} ",SHARE_DB.size(),atomicInteger.get());
    }

    private void getCountAndThreadName() {
        System.out.println(Thread.activeCount());
        Thread[] list  = new Thread[Thread.activeCount()];
        Thread.currentThread().getThreadGroup().enumerate(list);
        for (int i = 0; i <list.length ; i++) {
            System.out.println(list[i].getName());
        }
    }
}
