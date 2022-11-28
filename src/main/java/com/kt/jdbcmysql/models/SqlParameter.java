package com.kt.jdbcmysql.models;

import lombok.Getter;

@Getter
public class SqlParameter {
    private String parameterName = null;
    private Object value;

    // using parameter name
    public SqlParameter(String parameterName, Object value) {
        this.parameterName = parameterName;
        this.value = value;
    }

    // using index
    public SqlParameter(Object value) {
        this.value = value;
    }
}
