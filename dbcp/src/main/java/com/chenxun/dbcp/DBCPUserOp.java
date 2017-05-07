package com.chenxun.dbcp;

import com.chenxun.jdbc.JDBCUtils;
import com.chenxun.jdbc.User;
import org.apache.log4j.Logger;

import java.sql.*;

public class DBCPUserOp {
	static Logger LOG = Logger.getLogger(DBCPUserOp.class);

	public void addUser_Statement(User user) {
		Connection connection = ConnectionPool.getConnection();
		ResultSet resultSet = null;
		PreparedStatement preparedStatement = null;
		try {
			String sql = "insert into user values (null,?,?,?,?)";
			preparedStatement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
			preparedStatement.setString(1, user.getName());
			preparedStatement.setString(2, user.getUsername());
			preparedStatement.setString(3, user.getPassword());
			preparedStatement.setString(4, user.getEmail());
			preparedStatement.execute();
			resultSet = preparedStatement.getGeneratedKeys();
			resultSet.next();
			long id = resultSet.getLong(1);
			LOG.info("[addUser_Statement] id=" + id);
		} catch (Exception e) {
			LOG.info("[addUser_Statement]", e);
		} finally {
			ConnectionPool.close(resultSet, preparedStatement, connection);
		}
	}
}
