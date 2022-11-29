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

    public void executeStoredProcedure(String sp, SqlParameter... params) throws SQLException {
        this.executeStoredProcedure(sp, false, params);
    }

    public List<Map<String, Object>> getSingleResultSet(String sp, SqlParameter... params) throws SQLException {
        final Statement statement = this.executeStoredProcedure(sp, true, params);
        return this.getSingleResultSet(statement, null);
    }

    public List<Map<String, Object>> getSingleResultSetWithRowMapper(String sp,
            List<String> rowMapper, SqlParameter... params)
            throws SQLException {
        final Statement statement = this.executeStoredProcedure(sp, true, params);
        return this.getSingleResultSet(statement, rowMapper);
    }

    public List<List<Map<String, Object>>> getMultipleResultSets(String sp, SqlParameter... params)
            throws SQLException {
        final Statement statement = this.executeStoredProcedure(sp, true, params);
        return this.getMultipleResultSets(statement, null);
    }

    public List<List<Map<String, Object>>> getMultipleResultSetsWithRowMappers(String sp,
            List<List<String>> rowMappers, SqlParameter... params)
            throws SQLException {
        final Statement statement = this.executeStoredProcedure(sp, true, params);
        return this.getMultipleResultSets(statement, rowMappers);
    }

    private Statement executeStoredProcedure(String sp, boolean returnStatement, SqlParameter... params)
            throws SQLException {
        final int paramsSize = params.length;
        sp = this.transformStoredProcedure(sp, paramsSize);
        final CallableStatement statement = this.connection.prepareCall(sp);

        for (int index = 0; index < paramsSize; index++) {
            final SqlParameter param = params[index];
            final int sqlParameterIndex = index + 1;
            this.setSqlParameter(statement, param, sqlParameterIndex);
        }

        statement.execute();

        if (returnStatement == false) {
            return null;
        }
        return statement;
    }

    private void setSqlParameter(CallableStatement statement, SqlParameter param, int sqlParameterIndex)
            throws SQLException {
        final Object value = param.getValue();
        final String parameterName = param.getParameterName();
        if (parameterName == null) {
            statement.setObject(sqlParameterIndex, value);
        } else {
            statement.setObject(parameterName, value);
        }
    }

    private List<List<Map<String, Object>>> getMultipleResultSets(Statement statement, List<List<String>> rowMappers)
            throws SQLException {
        final List<List<Map<String, Object>>> multipleResultSets = new ArrayList<>();
        int rowMapperIndex = 0;
        boolean hasNextResultSet = true;

        while (hasNextResultSet) {
            List<Map<String, Object>> singleResultSet;
            if (rowMappers == null || rowMappers.size() == 0) {
                singleResultSet = this.getSingleResultSet(statement, null);
            } else {
                singleResultSet = this.getSingleResultSet(statement, rowMappers.get(rowMapperIndex));
                rowMapperIndex++;
            }

            multipleResultSets.add(singleResultSet);
            hasNextResultSet = statement.getMoreResults();
        }

        return multipleResultSets;
    }

    private List<Map<String, Object>> getSingleResultSet(Statement statement, List<String> rowMapper)
            throws SQLException {
        final ResultSet rs = statement.getResultSet();
        final List<Map<String, Object>> singleResultSet = new ArrayList<>();
        while (rs.next()) {
            final Map<String, Object> row = (rowMapper == null || rowMapper.size() == 0)
                    ? this.getRowWithAllColumns(rs)
                    : this.getRowWithRowMapper(rs, rowMapper);
            singleResultSet.add(row);
        }
        return singleResultSet;
    }

    private Map<String, Object> getRowWithAllColumns(ResultSet rs) throws SQLException {
        final Map<String, Object> row = new HashMap<>();
        final ResultSetMetaData rsMetaData = rs.getMetaData();
        for (int i = 1; i <= rsMetaData.getColumnCount(); i++) {
            final String columnName = rsMetaData.getColumnName(i);
            row.put(columnName, rs.getObject(columnName));
        }
        return row;
    }

    private Map<String, Object> getRowWithRowMapper(ResultSet rs, List<String> rowMapper)
            throws SQLException {
        final Map<String, Object> row = new HashMap<>();
        for (final String columnName : rowMapper) {
            row.put(columnName, rs.getObject(columnName));
        }
        return row;
    }

    private String transformStoredProcedure(String sp, int paramsSize) {
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
}
