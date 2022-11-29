package com.kt.jdbcmysql.service;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.kt.jdbcmysql.models.SqlParameter;

@Service
public class JdbcService {

    @Autowired
    private Connection connection;

    public void executeStoredProcedureVoid(String sp, SqlParameter... params) throws SQLException {
        this.executeStoredProcedure(sp, false, params);
    }

    public ResultSet executeStoredProcedureResultSet(String sp, SqlParameter... params) throws SQLException {
        final Statement statement = this.executeStoredProcedure(sp, true, params);
        return statement.getResultSet();
    }

    public List<List<Map<String, Object>>> executeStoredProcedureResultSets(String sp, SqlParameter... params)
            throws SQLException {
        final Statement statement = this.executeStoredProcedure(sp, true, params);
        return this.transformResultSetsToMap(statement);
    }

    /*
     * PRIVATE METHODS (UTILITIES)
     */
    private Statement executeStoredProcedure(String sp, boolean returnRs, SqlParameter... params)
            throws SQLException {
        final int paramSize = params.length;
        sp = this.transformSp(sp, paramSize);
        final CallableStatement statement = this.connection.prepareCall(sp);

        for (int index = 0; index < paramSize; index++) {
            final SqlParameter param = params[index];
            final int sqlParameterIndex = index + 1;
            this.setParameterIntoStatement(statement, param, sqlParameterIndex);
        }

        statement.execute();

        if (returnRs == false) {
            return null;
        } else {
            return statement;
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

    private void setParameterIntoStatement(CallableStatement statement, SqlParameter param, int sqlParameterIndex)
            throws SQLException {
        final Object value = param.getValue();
        final String parameterName = param.getParameterName();

        if (parameterName == null) {
            statement.setObject(sqlParameterIndex, value);
        } else {
            statement.setObject(parameterName, value);
        }
    }

    private List<List<Map<String, Object>>> transformResultSetsToMap(Statement statement) throws SQLException {
        final List<List<Map<String, Object>>> resultSetsMap = new ArrayList<>();
        boolean hasNextResultSet = true;
        while (hasNextResultSet) {
            final ResultSet rs = statement.getResultSet();
            final List<Map<String, Object>> resultSetMap = new ArrayList<>();

            while (rs.next()) {
                final Map<String, Object> sqlRow = new HashMap<>();
                final ResultSetMetaData rsMetaData = rs.getMetaData();

                for (int i = 1; i <= rsMetaData.getColumnCount(); i++) {
                    final String columnName = rsMetaData.getColumnName(i);
                    sqlRow.put(columnName, rs.getObject(columnName));
                }
                resultSetMap.add(sqlRow);
            }

            resultSetsMap.add(resultSetMap);
            hasNextResultSet = statement.getMoreResults();
        }

        return resultSetsMap;
    }
}
