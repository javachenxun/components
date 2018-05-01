package com.chenxun.components.spring;

import com.chenxun.components.spring.service.UserService;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * Created by chenxun on 2017/7/3 0:46
 */
public class UserTest {
    final Log LOG = LogFactory.getLog(UserTest.class);
    @Test
    public  void run(){
        ApplicationContext context =  new ClassPathXmlApplicationContext(new String[] {"services.xml"});
        UserService user = context.getBean("user", UserService.class);
        LOG.info("name="+user.getName());
    }
}
