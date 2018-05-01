package com.chenxun.components.dbcp.jdbc;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class UserOp {

	public void addUser_Statement(User user) {
		Connection conn = null;
		Statement stmt = null;
		ResultSet rs = null;
		try {
			conn = JDBCUtils.getConnection();
			String sql = "insert into user values (null,'" + user.getName()
					+ "','" + user.getUsername() + "','" + user.getPassword()
					+ "','" + user.getEmail() + "')";
			stmt = conn.createStatement();
			stmt.executeUpdate(sql);
			rs = stmt.getGeneratedKeys();
			rs.next();
			long long1 = rs.getLong(1);
			System.out.println(long1);
			
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			JDBCUtils.release(rs,stmt,conn);
		}
	}

	public void addUser_PreparedStatement(User user) {
		Connection conn = null;
		PreparedStatement stmt= null;
		ResultSet rs = null;
		try {
			conn = JDBCUtils.getConnection();
			String sql = "insert into user values (null,?,?,?,?)";
			stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
			stmt.setString(1, user.getName());
			stmt.setString(2, user.getUsername());
			stmt.setString(3,  user.getPassword());
			stmt.setString(4,  user.getEmail());
			stmt.execute();
			rs = stmt.getGeneratedKeys();
			rs.next();
			long long1 = rs.getLong(1);
			System.out.println(long1);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			JDBCUtils.release(rs,stmt,conn);
		}
	}

	public void modifyUser(User user) {
		Connection conn = null;
		Statement stmt = null;
		try {
			conn = JDBCUtils.getConnection();
			String sql = "update user set name='" + user.getName()
					+ "',username='" + user.getUsername() + "',password='"
					+ user.getPassword() + "',email='" + user.getEmail()
					+ "' where id = " + user.getId();
			stmt = conn.createStatement();
			stmt.executeUpdate(sql);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			JDBCUtils.release(null,stmt,conn);
		}
	}

	public void deleteUser(int id) {
		Connection conn = null;
		Statement stmt = null;
		try {
			conn = JDBCUtils.getConnection();
			String sql = "delete from user where id = " + id;
			stmt = conn.createStatement();
			stmt.executeUpdate(sql);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			JDBCUtils.release(null,stmt,conn);
		}
	}

	public List<User> findAll() {
		List<User> list = new ArrayList<User>();
		Connection conn = null;
		Statement stmt = null;
		ResultSet rs = null;
		try{
			conn = JDBCUtils.getConnection();
			String sql = "select * from user";
			stmt = conn.createStatement();
			rs = stmt.executeQuery(sql);
			while(rs.next()){
				User user = new User();
				user.setId(rs.getLong("id"));
				user.setName(rs.getString("name"));
				user.setUsername(rs.getString("username"));
				user.setPassword(rs.getString("password"));
				user.setEmail(rs.getString("email"));
				
				list.add(user);
			}
		}catch(Exception e){
			e.printStackTrace();
		}finally {
			JDBCUtils.release(rs,stmt,conn);
		}
		return list;
	}

	public User findById(int id) {
		User user = new User();
		Connection conn = null;
		Statement stmt = null;
		ResultSet rs = null;
		try{
			conn = JDBCUtils.getConnection();
			String sql = "select * from user where id = "+id;
			stmt = conn.createStatement();
			rs = stmt.executeQuery(sql);
			if(rs.next()){
				user.setId(rs.getLong("id"));
				user.setName(rs.getString("name"));
				user.setUsername(rs.getString("username"));
				user.setPassword(rs.getString("password"));
				user.setEmail(rs.getString("email"));
			}
		}catch(Exception e){
			e.printStackTrace();
		}finally {
			JDBCUtils.release(rs,stmt,conn);
		}
		return user;
	}

}
