package com.kt.jdbcmysql;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

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
			jdbcService.createPersonTable();

			jdbcService.insertPerson("John", "Dolby", 30);
			jdbcService.insertPerson("Ping", "Ping", 24);
			jdbcService.insertPerson("Larson", "Velgus", 18);
			jdbcService.insertPerson("Alaba", "Westdune", 57);
			jdbcService.printAllPeople();

			jdbcService.dropPersonTable();
		};
	}
}
