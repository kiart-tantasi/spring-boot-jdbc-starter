package com.kt.jdbcmysql;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import com.kt.jdbcmysql.models.SqlParameter;
import com.kt.jdbcmysql.service.JdbcService;

@SpringBootApplication
public class JdbcMysqlApplication {

	@Autowired
	private JdbcService jdbcService;

	public static void main(String[] args) {
		SpringApplication.run(JdbcMysqlApplication.class, args);
	}

	@Bean
	CommandLineRunner commandLineRunner() {
		return args -> {

			// 25 year old
			System.out.println("\nEmployees who are 25 year old:");
			ResultSet rs = this.jdbcService.executeSpResultSet("get_by_age",
					new SqlParameter(25));
			while (rs.next()) {
				printPerson(rs);
			}

			// Engineers
			System.out.println("\nEmployees who work as Engineers:");
			rs = this.jdbcService.executeSpResultSet("get_by_career",
					new SqlParameter("Engineer"));
			while (rs.next()) {
				printPerson(rs);
			}

			// Engineers who age at 25
			System.out.println("\nEmployees who are 25 year old and work as Engineers:");
			rs = this.jdbcService.executeSpResultSet("get_by_age_and_career",
					new SqlParameter(25),
					new SqlParameter("Engineer"));
			while (rs.next()) {
				printPerson(rs);
			}

			// All employees
			printAllEmployees();

			// Insert new employee named Joseph who ages 29 and works as a Data Scientist.
			System.out.println("\nInsert new employee...");
			this.jdbcService.executeSpVoid("insert_employee",
					new SqlParameter("Joseph"),
					new SqlParameter(29),
					new SqlParameter("Data Scientist"));

			// All employees
			printAllEmployees();

			// Deleting Joseph
			this.jdbcService.executeSpVoid("delete_employee",
					new SqlParameter("Joseph"),
					new SqlParameter(29),
					new SqlParameter("Data Scientist"));
		};
	}

	private void printPerson(ResultSet rs) throws SQLException {
		System.out.println(String.format("%s, working as %s, aged %s", rs.getString("name"), rs.getString("career"),
				rs.getInt("age")));
	}

	private void printAllEmployees() throws SQLException {
		System.out.println("\nAll employees:");
		ResultSet rs = this.jdbcService.executeSpResultSet("get_all",
				new SqlParameter[] {});
		while (rs.next()) {
			printPerson(rs);
		}
	}
}
