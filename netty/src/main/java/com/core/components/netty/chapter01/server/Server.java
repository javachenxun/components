package com.core.components.netty.chapter01.server;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by chenxun.
 * Date: 2018/12/3 下午11:45
 * Description:
 */
public class Server {

    Logger logger = LoggerFactory.getLogger(Server.class);

    private AtomicInteger client = new AtomicInteger(0);

    private ServerSocket serverSocket = null;

    public Server(int port) {
        try {
            serverSocket = new ServerSocket(port);
            logger.info("  msg=服务端启动成功 port={}", port);
        } catch (Exception e) {
            logger.error(" msg=服务端启动异常 ", e);
        }
    }

    public void start() {
        new Thread(() -> {
            while (true) {
                try {
                    Socket socket = serverSocket.accept();
                    new ClientHandler(socket,client.getAndIncrement()).start();
                } catch (Exception e) {
                    logger.error(" act=start  msg=服务端启动异常 ", e);
                }
            }
        }).start();


    }
}