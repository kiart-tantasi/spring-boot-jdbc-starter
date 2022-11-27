package com.kt.jdbcmysql.service;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.kt.jdbcmysql.models.SqlParameter;

@Service
public class JdbcService {

    @Autowired
    private Connection connection;

    public ResultSet executeStoredProcedure(String sp, SqlParameter ...params) throws SQLException{
        // initialize a statement
        CallableStatement statement = this.connection.prepareCall(sp);

        // set up parameters into statement
        final List<SqlParameter> list = List.of(params);        
        for (int i = 0; i < list.size(); i++) {
            final SqlParameter param = list.get(i);
            Object value = param.getValue();

            if (value instanceof String) {
                statement.setString((i + 1), (String) value);
            } else if (value instanceof Integer) {
                statement.setInt((i + 1), (Integer) value);
            }
        }

        // execute
        statement.execute();

        // return result set
        return statement.getResultSet();
    }
}
