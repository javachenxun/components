package com.core.components.dbcp.dbcp;

import org.apache.commons.dbcp2.BasicDataSource;
import org.apache.commons.dbcp2.BasicDataSourceFactory;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Properties;

/**
 * Created by chenxun on 2017/4/23.
 */
public class ConnectionPool {

    static final Log LOG = LogFactory.getLog(ConnectionPool.class);

    private ConnectionPool() {
    }

    private static BasicDataSource basicDataSource = null;

    static {
        init();
    }

    private static void init() {
        InputStream is = null;
        Properties properties = properties = new Properties();
        try {
            is = ConnectionPool.class.getResourceAsStream("/dbcp.properties");
            properties.load(is);
            basicDataSource = BasicDataSourceFactory.createDataSource(properties);
        } catch (Exception e) {
            LOG.error("[init]", e);
        } finally {
            if (is != null) {
                try {
                    is.close();
                } catch (IOException e) {
                }
                is = null;
            }
        }
    }

    public static Connection getConnection() {
        try {
            if(basicDataSource==null)
                init();
            return basicDataSource.getConnection();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static void  close(ResultSet resultSet,PreparedStatement preparedStatement,Connection connection){
        if (resultSet!=null){
            try {
                resultSet.close();
            } catch (SQLException ingored) {
            }finally {
                resultSet=null;
            }
        }
        if (preparedStatement!=null){
            try {
                preparedStatement.close();
            } catch (SQLException ingored) {
            }finally {
                preparedStatement=null;
            }
        }
        if (connection!=null){
            try {
                connection.close();
            } catch (SQLException ingored) {
            }finally {
                connection=null;
            }
        }
    }
}
