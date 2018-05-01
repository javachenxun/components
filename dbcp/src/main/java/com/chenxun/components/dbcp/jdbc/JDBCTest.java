package com.chenxun.components.dbcp.jdbc;


import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Properties;


/**
 * JDBC的测试类
 * @author chenxun
 */
public class JDBCTest {

	private Properties pro = null;
	private String driverClass = null;
	private String url = null;
	private String username = null;
	private String password = null;

	@Before
	public void getProperties() throws IOException {
		InputStream is = JDBCTest.class.getClassLoader().getResourceAsStream("db.properties");
		pro = new Properties();
		pro.load(is);
		driverClass = (String) pro.get("driverClass");
		url = (String) pro.get("url");
		username = (String) pro.get("username");
		password = (String) pro.get("password");
	}
	@Test
	public void demo1() throws Exception {
		/**
		 * 1.加载驱动 2.获得连接 3.操作数据库 4.释放资源
		 */
		// DriverManager.registerDriver(new Driver());
		Class.forName(driverClass);
		Connection conn = DriverManager.getConnection(url,username,password);
		String sql = "select * from user";
		Statement stmt = conn.createStatement();
		ResultSet rs = stmt.executeQuery(sql);
		while (rs.next()) {
			String result = String.format("id=%d name=%s username=%s  password=%s email=%s",
			rs.getInt("id"),
			rs.getString("name"),
			rs.getString("username"),
			rs.getString("password"),
			rs.getString("email")
			);
			System.out.println(result);
		}
		rs.close();
		stmt.close();
		conn.close();
	}
	/**
	 * 1.事务相关显示设置
	 * 	conn.getAutoCommit()
	 * 	conn.setAutoCommit(false);
	 * 	conn.commit();
	 * 2.测试滚动结果集
	 */
	@Test
	public void demo2() throws Exception {
		Class.forName(driverClass);
		Connection conn = DriverManager.getConnection(url, username, password);
		String sql = "select * from user";
		// 创建执行sql语句的对象. --- 结果集 可以滚动而且支持修改.
		Statement stmt = conn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
		ResultSet rs = stmt.executeQuery(sql);
		// 直接到第n行.
		rs.absolute(2);
		rs.updateString("name", "heihei3");
		rs.updateRow();

		rs.close();
		stmt.close();
		conn.close();
	}
}
