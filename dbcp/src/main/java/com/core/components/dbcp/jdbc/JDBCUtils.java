package com.core.components.dbcp.jdbc;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ResourceBundle;

public class JDBCUtils {

	private static final String DRIVERCLASS; 
	private static final String URL; 
	private static final String USERNAME; 
	private static final String PASSWORD ; 
	
	static{
		DRIVERCLASS = ResourceBundle.getBundle("db").getString("driverClass");
		URL = ResourceBundle.getBundle("db").getString("url");
		USERNAME = ResourceBundle.getBundle("db").getString("username");
		PASSWORD = ResourceBundle.getBundle("db").getString("password");
	}
	/**
	 * 加载驱动的方法.
	 */
	private static void loadDriver(){
		try {
			Class.forName(DRIVERCLASS);
		} catch (ClassNotFoundException e) {
			throw new RuntimeException(e);
		}
	}
	
	/**
	 * 获得连接
	 *  SQLException
	 */
	public static Connection getConnection()  {
		loadDriver();
		Connection conn = null;
		try {
			conn = DriverManager.getConnection(URL, USERNAME, PASSWORD);
		} catch (SQLException e) {
			throw new RuntimeException("获取连接异常",e);
		}
		return conn;
	}
	/**
	 * 释放资源.
	 */
	public static void release(ResultSet rs, Statement stmt, Connection conn){
		release(rs);
		release(stmt);
		release(conn);
	}
	private static void release(ResultSet rs){
		if (rs != null) {
	        try {
	            rs.close();
	        } catch (SQLException sqlEx) {
				//ingored
	        }finally {
				rs = null;
			}
	    }
	}

	private static void release(Statement stmt){
		if (stmt != null) {
	        try {
	            stmt.close();
	        } catch (SQLException sqlEx) {
				//ingored
	        }finally {
				stmt = null;
			}
	    }
	}

	private static void release(Connection conn){
		if(conn != null){
	    	try {
				conn.close();
			} catch (SQLException e) {
				//ingored
			}finally {
				conn = null;
			}
	    }
	}
}
