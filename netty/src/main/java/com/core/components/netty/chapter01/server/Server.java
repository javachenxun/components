package com.core.components.netty.chapter01.server;

import com.core.components.netty.Const;

/**
 * Created by chenxun.
 * Date: 2018/12/3 下午11:45
 * Description:
 */
public class Server {


    public static void main(String[] args) {
        new ServerBootstrap(Const.PRORT).start();
    }
}