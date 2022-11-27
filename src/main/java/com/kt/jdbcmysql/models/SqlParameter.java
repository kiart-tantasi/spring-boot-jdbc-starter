package com.kt.jdbcmysql.models;

import lombok.Getter;

@Getter
public class SqlParameter {
    private final Object value;

    public SqlParameter(Object value) {
        this.value = value;
    }
}
