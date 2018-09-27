package jdbc.test;

import com.chenxun.components.dbcp.jdbc.User;
import com.chenxun.components.dbcp.jdbc.UserOp;
import org.junit.Test;

import java.util.List;

public class UserTest {

	@Test
	public void demo1(){
		User user = new User("chenxun","zs","123","123@qq.cn");
		UserOp userDao = new UserOp();
		userDao.addUser_PreparedStatement(user);
	}
	
	@Test
	public void demo2(){
		UserOp userDao = new UserOp();
		User user = userDao.findById(28);
		user.setName("aa");
		userDao.modifyUser(user);
	}
	
	@Test
	public void demo3(){
		UserOp userDao = new UserOp();
		userDao.deleteUser(28);
	}
	
	@Test
	public void demo4(){
		UserOp userDao = new UserOp();
		List<User> list = userDao.findAll();
		for (User user : list) {
			System.out.println(user.getName()+"   "+user.getPassword());
		}
	}
}
