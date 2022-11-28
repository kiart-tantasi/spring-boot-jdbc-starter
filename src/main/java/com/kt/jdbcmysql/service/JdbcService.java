package com.kt.jdbcmysql.service;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.kt.jdbcmysql.models.SqlParameter;

@Service
public class JdbcService {

    @Autowired
    private Connection connection;

    public void executeStoredProcedureVoid(String sp, SqlParameter... params) throws SQLException {
        this.executeSpHelper(sp, false, params);
    }

    public ResultSet executeStoredProcedureResultSet(String sp, SqlParameter... params) throws SQLException {
        return this.executeSpHelper(sp, true, params);
    }

    public ResultSet[] executeStoredProcedureResultSets(String sp, SqlParameter... params) throws SQLException {
        return new ResultSet[] {};
    }

    /*
     * PRIVATE METHODS (UTILITIES)
     */
    private ResultSet executeSpHelper(String sp, boolean returnRs, SqlParameter... params) throws SQLException {
        final int paramSize = params.length;
        sp = this.transformSp(sp, paramSize);
        final CallableStatement statement = this.connection.prepareCall(sp);

        for (int index = 0; index < paramSize; index++) {
            final Object value = params[index].getValue();
            this.setParameterIntoStatement(statement, value, (index + 1));
        }

        statement.execute();

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
