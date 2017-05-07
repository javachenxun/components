package com.chenxun.dbcp;

import com.chenxun.jdbc.User;
import com.chenxun.jdbc.UserOp;
import org.junit.Test;

import java.util.HashSet;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class DBCPUserTest {

	@Test
	public void demo1() throws InterruptedException {
		User user = new User("chenxun","zs","123","123@qq.cn");
		DBCPUserOp userDao= new DBCPUserOp();
		for (int i =0;i<30;i++){
			new Runnable() {
				@Override
				public void run() {
					userDao.addUser_Statement(user);
				}
			}.run();
		}
		TimeUnit.SECONDS.sleep(1);
		System.out.println(Thread.activeCount());
		while (Thread.activeCount()>3){
			Thread.yield();
		}
	}
}
