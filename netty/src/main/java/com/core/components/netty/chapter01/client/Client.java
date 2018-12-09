package com.core.components.netty.chapter01.client;

import com.core.components.netty.Const;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.InputStream;
import java.net.Socket;
import java.time.Duration;
import java.time.Instant;
import java.time.LocalTime;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by chenxun.
 * Date: 2018/12/3 下午11:46
 * Description:
 */
public class Client {

    static Logger logger = LoggerFactory.getLogger(Client.class);

    public static void main(String[] args) throws InterruptedException {
        LocalTime localTime = LocalTime.now();
        Instant instant = Instant.now();
        int threadNum = 300;
        CountDownLatch countDownLatch = new CountDownLatch(threadNum);
        for (int i = 0; i < threadNum; i++) {
            logger.info(" msg=createClient threadNum={}",i);
            final int clientNum = i;
            new Thread(() -> {
                try {
                    startOneClinet(clientNum);
                } catch (Exception e) {
                    logger.error(" act=startOneClinet ", e);
                } finally {
                    countDownLatch.countDown();
                }
            }).start();
        }
        while (countDownLatch.getCount()>0){
        }
        TimeUnit.SECONDS.sleep(1);
        logger.info(" start={} end={} duration={}",localTime,LocalTime.now(), Duration.between(instant,Instant.now()).toMillis()-1000);
    }

    private static void startOneClinet(int clientNum) {
        try {
            Socket socket = new Socket(Const.IP, Const.PRORT);
            AtomicInteger messageCount = new AtomicInteger();
            AtomicBoolean closeSignal = new AtomicBoolean(Boolean.FALSE);
            new Thread(() -> {
                for (; ; ) {
                    if (!closeSignal.get()) {
                        try {
                            String data = "hello word";
                            socket.getOutputStream().write(data.getBytes());
                            logger.info(" msg=客户端{}发送数据 data={} messageCount={}", clientNum, data, messageCount.incrementAndGet());
                            TimeUnit.SECONDS.sleep(1);
                        } catch (Exception e) {
                            logger.error(" msg=客户端发送数据异常 ", e);
                        }
                    } else {
                        try {
                            socket.shutdownOutput();
                            logger.info(" act=Sender msg=客户端{}关闭", clientNum);
                        } catch (Exception e) {
                            logger.error(" msg=shutdownInput ", e);
                        }
                        break;
                    }
                }

            }).start();

            new Thread(() -> {
                try {
                    for (;;) {
                        InputStream inputStream = socket.getInputStream();
                        byte[] data = new byte[1024];
                        int len;
                        while ((len = inputStream.read(data)) != -1) {
                            String message = new String(data, 0, len);
                            logger.info(" msg=客户端{}收到消息回应 message={}", clientNum, message);
                            logger.info(" ==================第{}次收到消息回应==============================", messageCount.get());
                        }
                        socket.shutdownInput();
                        logger.info(" act=Reciver  msg=客户端{}关闭", clientNum);
                        break;
                    }
                } catch (Exception e) {
                    logger.error(" msg=客户端接受数据异常 ", e);
                }
            }).start();
            TimeUnit.SECONDS.sleep(3);
            closeSignal.set(Boolean.TRUE);
            logger.info(" msg=给客户端{}发送关闭信号", clientNum);
        } catch (Exception e) {
            logger.error(" msg=客户端启动异常 ", e);
        }
    }

}