package com.chenxun.service;

import org.apache.commons.logging.Log;
import org.apache.log4j.Logger;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * Created by chenxun on 2017/7/3 0:46
 */
public class UserTest {
    final Logger LOG = Logger.getLogger(UserTest.class);
    @Test
    public  void run(){
        ApplicationContext context =  new ClassPathXmlApplicationContext(new String[] {"services.xml"});
        UserService user = context.getBean("user", UserService.class);
        ;
        LOG.info("name="+user.getName());
    }
}
