package com.kt.jdbcmysql.models;

import lombok.Getter;

@Getter
public class SqlParameter<T> {
    private final T value;

    public SqlParameter (T value) {
        this.value = value;
    }
}
