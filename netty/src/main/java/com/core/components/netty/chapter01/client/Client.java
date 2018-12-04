package com.core.components.netty.chapter01.client;

import com.core.components.netty.chapter01.server.Server;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.InputStream;
import java.net.Socket;
import java.util.concurrent.TimeUnit;

/**
 * Created by chenxun.
 * Date: 2018/12/3 下午11:46
 * Description:
 */
public class Client {

    static Logger logger = LoggerFactory.getLogger(Server.class);

    private static  String ip = "127.0.0.1";
    private static  int prort = 8080;

    public static void main(String[] args) {
        for (int i = 0; i < 5; i++) {
            startOneClinet(i);
        }
    }

    private static void startOneClinet(int clientNum) {
        try {
            Socket socket = new Socket(ip,prort);
            new Thread(()->{
                while (true){
                    try {
                        String data = "hello word";
                        socket.getOutputStream().write(data.getBytes());
                        logger.info(" msg=客户端{}发送数据 data={}",clientNum,data);
                        TimeUnit.SECONDS.sleep(2);
                    }catch (Exception e){
                        logger.error(" msg=客户端发送数据异常 ",e);
                    }
                }
            }).start();

            new Thread(()-> {
                while (true) {
                    try {
                        InputStream inputStream = socket.getInputStream();
                        byte[] data = new byte[1024];
                        int len;
                        while ((len = inputStream.read(data)) != -1) {
                            String message = new String(data, 0, len);
                            logger.info(" msg=客户端{}收到消息回应 message={}",clientNum, message);
                        }
                    } catch (Exception e) {
                        logger.error(" msg=客户端接受数据异常 ", e);
                    }
                }
            }).start();
        }catch (Exception e){
            logger.error(" msg=客户端启动异常 ",e);
        }
    }

}