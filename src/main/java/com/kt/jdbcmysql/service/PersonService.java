package com.kt.jdbcmysql.service;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PersonService {

    private Connection connection;

    private Statement statement;

    public PersonService(@Autowired Connection connection) throws SQLException {
        this.connection = connection;
        this.statement = connection.createStatement();
    }

    public void demoThisService() throws SQLException {
        System.out.println("\nStarting demoing Person service...\n");
        this.createPersonTable();
        this.insertPerson("David", "Beckham", 47);
        this.insertPerson("Prayut", "Chanocha", 8);
        this.insertPerson("Marika", "Nuanjun", 32);
        this.printAllPeople();
        this.dropPersonTable();
    }

    public void createPersonTable() throws SQLException {
        statement
                .execute("create table if not exists person (firstname varchar(254), lastname varchar(254), age int);");
    }

    public void dropPersonTable() throws SQLException {
        statement.execute("drop table if exists person;");
    }

    public void insertPerson(String firstname, String lastname, int age) throws SQLException {
        PreparedStatement statement = connection
                .prepareStatement("insert into person (firstname, lastname, age) values (?,?,?);");
        statement.setString(1, firstname);
        statement.setString(2, lastname);
        statement.setInt(3, age);
        final int affeted = statement.executeUpdate();
        if (affeted > 0) {
            System.out.println("Inserting person succeeded...");
        } else {
            System.out.println("Inserting person failed !!!!");
        }
    }

    public void printAllPeople() throws SQLException {
        final ResultSet rs = statement.executeQuery("select * from person;");
        System.out.println("\nPeople:");
        while (rs.next()) {
            final String msg = String.format("%s %s - %s", rs.getString("firstname"), rs.getString("lastname"),
                    rs.getInt("age"));
            System.out.println(msg);
        }
    }
}
