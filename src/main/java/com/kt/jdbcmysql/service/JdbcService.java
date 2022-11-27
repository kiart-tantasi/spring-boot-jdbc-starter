package com.kt.jdbcmysql.service;

import java.io.InputStream;
import java.math.BigDecimal;
import java.sql.Blob;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
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
        switch (value.getClass().getSimpleName()) {

            // String
            case "String":
                statement.setString(index, (String) value);
                break;

            // Number
            case "Integer":
                statement.setInt(index, (int) value);
                break;
            case "Long":
                statement.setLong(index, (long) value);
                break;
            case "Double":
                statement.setDouble(index, (double) value);
                break;
            case "Float":
                statement.setFloat(index, (float) value);
                break;
            case "BigDecimal":
                statement.setBigDecimal(index, (BigDecimal) value);
                break;

            // Time
            case "Date":
                statement.setDate(index, (Date) value);
                break;
            case "Timestamp":
                statement.setTimestamp(index, (Timestamp) value);
                break;

            // Boolean
            case "Boolean":
                statement.setBoolean(index, (boolean) value);
                break;

            // Files
            case "Byte":
                statement.setByte(index, (byte) value);
                break;
            case "Byte[]":
                statement.setBytes(index, (byte[]) value);
                break;
            case "InputStream":
                statement.setBinaryStream(index, (InputStream) value);
                break;
            case "Blob":
                statement.setBlob(index, (Blob) value);
                break;

            // Others
            default:
                throw new RuntimeException("Data type is not or not yet accepted as parameter.");
        }
    }
}
