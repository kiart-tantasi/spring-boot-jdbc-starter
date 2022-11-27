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

    public void executeSpVoid(String sp, SqlParameter... params) throws SQLException {
        this.executeSpHelper(sp, false, params);
    }

    public ResultSet executeSpResultSet(String sp, SqlParameter... params) throws SQLException {
        return this.executeSpHelper(sp, true, params);
    }

    private ResultSet executeSpHelper(String sp, boolean returnRs, SqlParameter... params) throws SQLException {
        final List<SqlParameter> list = List.of(params);
        final int paramSize = list.size();

        // transform sp to format `{ call sp_name(?) }`
        sp = this.transformSp(sp, paramSize);

        // initialize a statement
        final CallableStatement statement = this.connection.prepareCall(sp);

        // set up parameters into statement
        for (int index = 0; index < paramSize; index++) {
            final SqlParameter param = list.get(index);
            final Object value = param.getValue();

            this.setParameterIntoStatement(statement, value, (index + 1));
        }

        // execute
        statement.execute();

        // return result set or void
        if (returnRs == true) {
            return statement.getResultSet();
        } else {
            return null;
        }
    }

    private String transformSp(String sp, int paramsSize) {
        String questionsMarks = "";
        for (int i = 0; i < paramsSize; i++) {
            if (i == (paramsSize - 1)) {
                questionsMarks += "?";
            } else {
                questionsMarks += "?, ";
            }
        }
        return String.format("{ call %s(%s) }", sp, questionsMarks);
    }

    private void setParameterIntoStatement(CallableStatement statement, Object value, int index) throws SQLException {
        statement.setObject(index, value);
    }
}
