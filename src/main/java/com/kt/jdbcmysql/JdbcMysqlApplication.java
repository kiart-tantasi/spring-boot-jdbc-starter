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
			System.out.println("\n25 year old:");
			ResultSet rs = jdbcService.executeStoredProcedure("{ call get_by_age(?) }",
			new SqlParameter<>(25));
			while (rs.next()) {
				printPerson(rs);
			}

			// Engineers
			System.out.println("\nEngineers:");
			rs = jdbcService.executeStoredProcedure("{ call get_by_career(?) }",
			new SqlParameter<>("Engineer"));
			while (rs.next()) {
				printPerson(rs);
			}
		};
	}

	private void printPerson(ResultSet rs) throws SQLException {
		System.out.println(String.format("%s, working as %s, aged %s", rs.getString("name"), rs.getString("career"), rs.getInt("age")));
	}
}
