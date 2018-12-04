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
public class ClientHandler {

    Logger logger = LoggerFactory.getLogger(Server.class);
    private  Socket socket;
    private  int client;

    public ClientHandler(Socket socket, int client) {
        this.socket = socket;
        this.client = client;
    }

    public void start() {
         new Thread(() -> {
             try {
                 InputStream inputStream = socket.getInputStream();
                 while (true) {
                     byte[] data = new byte[1024];
                     int len;
                     while ((len = inputStream.read(data)) != -1) {
                         String message = new String(data, 0, len);
                         logger.info(" msg=收到客户端{}消息 message={}",client, message);
                         socket.getOutputStream().write(new String("服务端收到客户端"+client+"消息:"+message).getBytes());
                     }
                 }
             } catch (Exception e) {
                 logger.error(" msg=服务端收到流失败 ", e);
             }

         }).start();
    }
}