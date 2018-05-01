### jedis version：2.9.0
##### 关于jedis基本使用
    1.基本使用
    2.添加多实例(分布式)多线程场景下的安全并发执行db问题：setnx+队列解决
    ps：单实例多线程执行，添加锁或同步代码块可以解决
        多实例单线程，使用分布式锁setnx即可解决+（对于队列中没有消费的任务，采用ScheduledThreadPoolExecutor启动线程定时做补偿）
        



