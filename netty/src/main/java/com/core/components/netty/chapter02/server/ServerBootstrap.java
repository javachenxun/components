package com.core.components.netty.chapter02.server;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by chenxun.
 * Date: 2018/12/3 下午11:45
 * Description:
 */
public class ServerBootstrap {

    Logger logger = LoggerFactory.getLogger(ServerBootstrap.class);

    private AtomicInteger client = new AtomicInteger(0);

    private ServerSocket serverSocket = null;

    public ServerBootstrap(int port) {
        try {
            serverSocket = new ServerSocket(port);
            logger.info("  msg=服务端启动成功 port={}", port);
        } catch (Exception e) {
            logger.error(" msg=服务端启动异常 ", e);
        }
    }
    private final static BlockingQueue<Runnable> workQueue = new LinkedBlockingDeque<>();
    private final static Executor executor = new ThreadPoolExecutor(5,5,3,TimeUnit.SECONDS,workQueue);
    public void start() {
        new Thread(() -> {
            while (true) {
                try {
                    Socket socket = serverSocket.accept();
                    executor.execute(new ServerOfClientHandler(socket,client.getAndIncrement(),workQueue));
                } catch (Exception e) {
                    logger.error(" act=start  msg=服务端启动异常 ", e);
                }
            }
        }).start();


    }
}