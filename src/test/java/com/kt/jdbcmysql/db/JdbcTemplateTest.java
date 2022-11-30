package com.kt.jdbcmysql.db;

import static org.junit.jupiter.api.Assertions.assertEquals;
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
    private final int VALUE_2 = 123;
    private final boolean VALUE_3 = false;

    @Mock
    private JdbcTemplateHelper jdbcTemplateHelper;

    @InjectMocks
    private JdbcTemplate jdbcTemplate;

    @Test
    void executeStoredProcedure() throws SQLException {
        when(jdbcTemplateHelper.executeStoredProcedure(SP_NAME, false))
            .thenReturn(null);
        jdbcTemplate.executeStoredProcedure(SP_NAME);
        verify(jdbcTemplateHelper).executeStoredProcedure(SP_NAME, false);
    }

    @Test
    void getSingleResultSet() throws SQLException {
        final Statement mockStatement = getMockStatement(false);
        when(jdbcTemplateHelper.executeStoredProcedure(SP_NAME, true))
                .thenReturn(mockStatement);

        final Map<String, Object> receivedRow = jdbcTemplate.getSingleResultSet(SP_NAME).get(0);
        assertEquals(VALUE_1, receivedRow.get(COLUMN_1));
        assertEquals(VALUE_2, receivedRow.get(COLUMN_2));
        assertEquals(VALUE_3, receivedRow.get(COLUMN_3));
    }

    @Test
    void getMultipleResultSets() throws SQLException {
        final int FIRST_RS_INDEX = 0;
        final int SECOND_RS_INDEX = 1;

        final Statement mockStatement = getMockStatement(true);
        when(jdbcTemplateHelper.executeStoredProcedure(SP_NAME, true))
                .thenReturn(mockStatement);

        final List<List<Map<String, Object>>> receivedResultSets = jdbcTemplate.getMultipleResultSets(SP_NAME);

        final Map<String, Object> firstRsFirstRow = receivedResultSets.get(FIRST_RS_INDEX).get(0);
        assertEquals(VALUE_1, firstRsFirstRow.get(COLUMN_1));
        assertEquals(VALUE_2, firstRsFirstRow.get(COLUMN_2));
        assertEquals(VALUE_3, firstRsFirstRow.get(COLUMN_3));

        final Map<String, Object> secondRsFirstRow = receivedResultSets.get(SECOND_RS_INDEX).get(0);
        assertEquals(VALUE_1, secondRsFirstRow.get(COLUMN_1));
        assertEquals(VALUE_2, secondRsFirstRow.get(COLUMN_2));
        assertEquals(VALUE_3, secondRsFirstRow.get(COLUMN_3));
    }

    // TODO: test method getSingleResultSetWithRowMapper()
    // @Test
    // void getSingleResultSetWithRowMapper() throws SQLException {
    // final Statement mockStatement = getMockStatement(false);
    // when(jdbcTemplateHelper.executeStoredProcedure(SP_NAME, true))
    // .thenReturn(mockStatement);

    // final List<String> rowMapper = List.of("col1", "col2");

    // final Map<String, Object> receivedRow =
    // jdbcTemplate.getSingleResultSetWithRowMapper(SP_NAME, rowMapper).get(0);
    // assertEquals(VALUE_1, receivedRow.get(COLUMN_1));
    // assertEquals(VALUE_2, receivedRow.get(COLUMN_2));
    // assertEquals(VALUE_3, receivedRow.get(COLUMN_3));
    // }

    // TODO: test method getMultipleResultSetsWithRowMappers()
    // @Test
    // void getMultipleResultSetsWithRowMappers() {
    // }

    /*
     * Private Utility Methods
     */
    private Statement getMockStatement(boolean multipleResultSets) throws SQLException {

        final ResultSetMetaData metaData = mock(ResultSetMetaData.class);
        when(metaData.getColumnCount()).thenReturn(3);
        when(metaData.getColumnName(1)).thenReturn(COLUMN_1);
        when(metaData.getColumnName(2)).thenReturn(COLUMN_2);
        when(metaData.getColumnName(3)).thenReturn(COLUMN_3);

        final ResultSet rs = mock(ResultSet.class);
        when(rs.getMetaData()).thenReturn(metaData);
        when(rs.getObject(COLUMN_1)).thenReturn(VALUE_1);
        when(rs.getObject(COLUMN_2)).thenReturn(VALUE_2);
        when(rs.getObject(COLUMN_3)).thenReturn(VALUE_3);

        if (multipleResultSets == true) {
            when(rs.next())
                    .thenReturn(true).thenReturn(false)
                    .thenReturn(true).thenReturn(false);
        } else {
            when(rs.next())
                    .thenReturn(true).thenReturn(false);
        }

        final Statement statement = mock(Statement.class);
        when(statement.getResultSet()).thenReturn(rs);

        if (multipleResultSets == true) {
            when(statement.getMoreResults()).thenReturn(true).thenReturn(false);
        } else {
            // no stubbing
        }

        return statement;
    }

}
