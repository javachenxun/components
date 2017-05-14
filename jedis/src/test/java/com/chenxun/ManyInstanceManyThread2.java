package com.chenxun;

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
public class ManyInstanceManyThread2 {

    private Logger LOG = LoggerFactory.getLogger(ManyInstanceManyThread2.class);

    private final  Map SHARE_DB = new HashMap(1);
    private final  String SHARE_KEY = "db";
    private final  String SHARE_KEY_L = "keylist";
    private final  String SHARE_VALUE = "value";
    private  final AtomicInteger doSize1Count = new AtomicInteger();
    private  final AtomicInteger doSizeManyCount = new AtomicInteger();
    private  final AtomicInteger addListCount = new AtomicInteger();
    private  final AtomicInteger getLoctCount = new AtomicInteger();

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
                            int i1 = getLoctCount.incrementAndGet();
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
                            int i1 = addListCount.incrementAndGet();
                        }
                        jedis.close();
                    }
                }).start();
            }
        }
        private void atomOp(int innerI) {
            if(SHARE_DB.size()==1){
                SHARE_DB.clear();
                int i1 = doSize1Count.incrementAndGet();
                SHARE_DB.put(SHARE_KEY + innerI, SHARE_VALUE + innerI);
            }else{
                int i1 = doSizeManyCount.incrementAndGet();
                SHARE_DB.put(SHARE_KEY + innerI, SHARE_VALUE + innerI);
            }
        }
    }

    @Test
    public  void test() throws InterruptedException {
        getCountAndThreadName();
        SHARE_DB.put(SHARE_KEY,SHARE_VALUE);
        new Thread(new Instance()).start();
        new Thread(new Instance()).start();
        while (Thread.activeCount()<5){
            Thread.yield();
        }
        while (Thread.activeCount()!=3){
            Thread.yield();
        }
        LOG.info("[test] share_db size={} doSize1Count={} doSizeManyCount={} addListCount={} getLoctCount={}",
                SHARE_DB.size(),doSize1Count.get(),doSizeManyCount.get(),addListCount.get(),getLoctCount.get());
    }

    @Test
    public  void batchTest() throws InterruptedException {
        for (int i = 0; i < 10; i++) {
            SHARE_DB.put(SHARE_KEY,SHARE_VALUE);
            new Thread(new Instance()).start();
            new Thread(new Instance()).start();
            new Thread(new Instance()).start();
            new Thread(new Instance()).start();
            while (Thread.activeCount()<5){
                Thread.yield();
            }
            while (Thread.activeCount()!=3){
                Thread.yield();
            }
            LOG.info("[test] share_db size={} doSize1Count={} doSizeManyCount={} addListCount={} getLoctCount={}",
                    SHARE_DB.size(),doSize1Count.get(),doSizeManyCount.get(),addListCount.get(),getLoctCount.get());
            //clear
            {
                SHARE_DB.clear();
                doSize1Count.set(0);
                doSizeManyCount.set(0);
                addListCount.set(0);
                getLoctCount.set(0);
            }
        }
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
