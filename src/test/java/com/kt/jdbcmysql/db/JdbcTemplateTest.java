package com.kt.jdbcmysql.db;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class JdbcTemplateTest {

    private final String SP_NAME = "sp";
    private final String COLUMN_1 = "column1";
    private final String COLUMN_2 = "column2";
    private final String COLUMN_3 = "column3";
    private final String VALUE_1 = "value";
    private final String VALUE_1_2 = "value2";
    private final int VALUE_2 = 123;
    private final int VALUE_2_2 = 1234;
    private final boolean VALUE_3 = false;
    private final boolean VALUE_3_2 = true;

    private Statement getMockStatement() throws SQLException {
        final ResultSetMetaData metaData = mock(ResultSetMetaData.class);
        when(metaData.getColumnCount()).thenReturn(3);
        when(metaData.getColumnName(1)).thenReturn(COLUMN_1);
        when(metaData.getColumnName(2)).thenReturn(COLUMN_2);
        when(metaData.getColumnName(3)).thenReturn(COLUMN_3);
        final ResultSet rs = mock(ResultSet.class);
        when(rs.getMetaData()).thenReturn(metaData);
        when(rs.getObject(COLUMN_1)).thenReturn(VALUE_1).thenReturn(VALUE_1_2);
        when(rs.getObject(COLUMN_2)).thenReturn(VALUE_2).thenReturn(VALUE_2_2);
        when(rs.getObject(COLUMN_3)).thenReturn(VALUE_3).thenReturn(VALUE_3_2);
        when(rs.next()).thenReturn(true).thenReturn(false);
        final Statement statement = mock(Statement.class);
        when(statement.getResultSet()).thenReturn(rs);
        return statement;
    }

    @Mock
    JdbcTemplateHelper jdbcTemplateHelper;

    @InjectMocks
    JdbcTemplate jdbcTemplate;

    @Test
    void testJdbcTemplate() {
        when(jdbcTemplateHelper.greeting()).thenReturn("TEST");
        assertEquals("TEST", jdbcTemplate.greeting());
    }

    @Test
    void executeStoredProcedure() throws SQLException {
        when(jdbcTemplateHelper.executeStoredProcedure(SP_NAME, false))
            .thenReturn(null);
        jdbcTemplate.executeStoredProcedure(SP_NAME);
        verify(jdbcTemplateHelper).executeStoredProcedure(SP_NAME, false);
    }

    @Test
    void getSingleResultSet() throws SQLException {
        final Statement mockStatement = this.getMockStatement();
        when(jdbcTemplateHelper.executeStoredProcedure(SP_NAME, true))
                .thenReturn(mockStatement);
        final Map<String, Object> receivedRow = jdbcTemplate.getSingleResultSet(SP_NAME).get(0);
        assertEquals(VALUE_1, receivedRow.get(COLUMN_1));
        assertEquals(VALUE_2, receivedRow.get(COLUMN_2));
        assertEquals(VALUE_3, receivedRow.get(COLUMN_3));
    }

    @Test
    void getMultipleResultSets() throws SQLException {
        final Statement mockStatement = this.getMockStatement();
        when(jdbcTemplateHelper.executeStoredProcedure(SP_NAME, true))
                .thenReturn(mockStatement);
        final List<Map<String, Object>> firstResultSet = jdbcTemplate.getMultipleResultSets(SP_NAME).get(0);
        assertNotNull(firstResultSet);
        // final List<Map<String, Object>> secondResultSet =
        // jdbcTemplate.getMultipleResultSets(SP_NAME).get(1);
    }
}

// public void executeStoredProcedure(final String sp, final SqlParameter...
// params) throws SQLException {
// this.jdbcTemplateHelper.executeStoredProcedure(sp, false, params);
// }

// public List<Map<String, Object>> getSingleResultSet(final String sp, final
// SqlParameter... params)
// throws SQLException {
// final Statement statement =
// this.jdbcTemplateHelper.executeStoredProcedure(sp, true, params);
// return this.getSingleResultSet(statement, null);
// }

// public List<Map<String, Object>> getSingleResultSetWithRowMapper(final String
// sp,
// final List<String> rowMapper, final SqlParameter... params)
// throws SQLException {
// final Statement statement =
// this.jdbcTemplateHelper.executeStoredProcedure(sp, true, params);
// return this.getSingleResultSet(statement, rowMapper);
// }

// public List<List<Map<String, Object>>> getMultipleResultSets(final String sp,
// final SqlParameter... params)
// throws SQLException {
// final Statement statement =
// this.jdbcTemplateHelper.executeStoredProcedure(sp, true, params);
// return this.getMultipleResultSets(statement, null);
// }

// public List<List<Map<String, Object>>>
// getMultipleResultSetsWithRowMappers(final String sp,
// final List<List<String>> rowMappers, final SqlParameter... params)
// throws SQLException {
// final Statement statement =
// this.jdbcTemplateHelper.executeStoredProcedure(sp, true, params);
// return this.getMultipleResultSets(statement, rowMappers);
// }
