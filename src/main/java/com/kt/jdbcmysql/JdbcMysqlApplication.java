package com.kt.jdbcmysql;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

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
			ResultSet rs = this.jdbcService.executeStoredProcedureResultSet("get_by_age",
					new SqlParameter("$age", 25));
			while (rs.next()) {
				printPerson(rs);
			}

			// Engineers who age at 25
			System.out.println("\nEmployees who are 25 year old and work as Engineers:");
			rs = this.jdbcService.executeStoredProcedureResultSet("get_by_age_and_career",
					new SqlParameter("$age", 25),
					new SqlParameter("$career", "Engineer"));
			while (rs.next()) {
				printPerson(rs);
			}

			// All employees
			this.printAllEmployees();

			// Insert new employee (Joseph, aged 29, Data Scientist)
			System.out.println("\nInserting Joseph as a new employee...");
			this.jdbcService.executeStoredProcedureVoid("insert_employee",
					new SqlParameter("$name", "Joseph"),
					new SqlParameter("$age", 29),
					new SqlParameter("$career", "Data Scientist"));

			// All employees
			this.printAllEmployees();

			// Deleting Joseph
			this.jdbcService.executeStoredProcedureVoid("delete_employee",
					new SqlParameter("$name", "Joseph"),
					new SqlParameter("$age", 29),
					new SqlParameter("$career", "Data Scientist"));

			// Getting Multiple Result Sets
			List<List<Map<String, Object>>> resultSetsMap = this.jdbcService.executeStoredProcedureResultSets(
					"get_careers_result_sets",
					new SqlParameter("$career_one", "Project Manager"),
					new SqlParameter("$career_two", "CTO"));
			this.printResultSetsMaps(resultSetsMap);
		};
	}

	private void printAllEmployees() throws SQLException {
		System.out.println("\nAll employees:");
		ResultSet rs = this.jdbcService.executeStoredProcedureResultSet("get_all",
				new SqlParameter[] {});
		while (rs.next()) {
			printPerson(rs);
		}
	}

	private void printResultSetsMaps(List<List<Map<String, Object>>> resultSetsMap) throws SQLException {
		System.out.println("\n[RESULT SETS]\n");
		for (final List<Map<String, Object>> resultSetMap : resultSetsMap) {
			System.out.println("Current result set:");
			for (final Map<String, Object> sqlRow : resultSetMap) {
				printPerson(sqlRow);
			}
			System.out.println("");
		}
	}

	private void printPerson(ResultSet rs) throws SQLException {
		System.out.println(String.format("%s, working as %s, aged %s", rs.getString("name"), rs.getString("career"),
				rs.getInt("age")));
	}

	private void printPerson(Map<String, Object> sqlRow) throws SQLException {
		System.out.println(String.format("%s, working as %s, aged %s", sqlRow.get("name"), sqlRow.get("career"),
				sqlRow.get("age")));
	}
}
