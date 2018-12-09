package com.core.components.netty.chapter01.server;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.InputStream;
import java.net.Socket;

/**
 * Created by chenxun.
 * Date: 2018/12/3 下午11:46
 * Description:
 */
public class ServerOfClientHandler {

    Logger logger = LoggerFactory.getLogger(ServerBootstrap.class);
    private  Socket socket;
    private  int client;

    public ServerOfClientHandler(Socket socket, int client) {
        this.socket = socket;
        this.client = client;
    }

    public void doStart() {
         new Thread(() -> {
             try {
                 for (;;) {
                     InputStream inputStream = socket.getInputStream();
                     byte[] data = new byte[1024];
                     int len;
                     while ((len = inputStream.read(data)) != -1) {
                         String message = new String(data, 0, len);
                         logger.info(" msg=收到客户端{}消息 message={}", client, message);
                         socket.getOutputStream().write(new String("服务端收到客户端" + client + "消息:" + message).getBytes());
                     }
                     socket.shutdownOutput();
                     logger.info(" msg=客户端{}关闭", client);
                     break;
                 }
                 logger.info(" msg=服务端消费线程结束 client={}",client);
             } catch (Exception e) {
                 logger.error(" msg=服务端收到流失败 ", e);
             }
         }).start();
    }
}