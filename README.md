####1.dbcp，version：2.2
#####1.1关于dbcp连接池的连接复用
    GenericObjectPool其中维护一个ConcurrentHashMap存放连接;org.apache.commons.dbcp2.PoolingDataSource.PoolGuardConnectionWrapper.close()
    覆写关闭连接。
#####1.2关于jedis基本使用
    1.基本使用
    2.添加多实例(分布式)多线程场景下的安全并发执行db问题：setnx+队列解决
    ps：单实例多线程执行，添加锁或同步代码块可以解决
        多实例单线程，使用分布式锁setnx即可解决
        



