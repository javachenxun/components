package com.core.components.netty.chapter01.server;

/**
 * Created by chenxun.
 * Date: 2018/12/3 下午11:45
 * Description:
 */
public class ServerBoot {

    static  int prort = 8080;

    public static void main(String[] args) {
        Server server = new Server(prort);
        server.start();
    }
}