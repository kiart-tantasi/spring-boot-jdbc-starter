package com.kt.jdbcmysql.db;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class JdbcTemplateTest {

    @Mock
    JdbcTemplateHelper jdbcTemplateHelper;

    @InjectMocks
    JdbcTemplate jdbcTemplate;

    @Test
    void testJdbcTemplate() {
        when(jdbcTemplateHelper.greeting()).thenReturn("TEST");
        assertEquals("TEST", jdbcTemplate.greeting());
    }

}
