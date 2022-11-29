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

			// Employees who work as engineers and are 25-year-old
			System.out.println("\nEmployees who are 25 year old and work as Engineers:");
			ResultSet rs = this.jdbcService.executeStoredProcedureResultSet("get_by_age_and_career",
					new SqlParameter("$age", 25),
					new SqlParameter("$career", "Engineer"));
			while (rs.next()) {
				printPerson(rs);
			}

			// Insert new employee (Joseph, aged 29, Data Scientist)
			System.out.println("\nInserting Joseph as a new employee...");
			this.jdbcService.executeStoredProcedure("insert_employee",
					new SqlParameter("$name", "Joseph"),
					new SqlParameter("$age", 29),
					new SqlParameter("$career", "Data Scientist"));

			// All employees
			System.out.println("\nAll employees:");
			rs = this.jdbcService.executeStoredProcedureResultSet("get_all",
					new SqlParameter[] {});
			while (rs.next()) {
				printPerson(rs);
			}

			// Deleting Joseph
			this.jdbcService.executeStoredProcedure("delete_employee",
					new SqlParameter("$name", "Joseph"),
					new SqlParameter("$age", 29),
					new SqlParameter("$career", "Data Scientist"));

			// Getting Multiple Result Sets Without Row Mappers (Returning All Columns)
			List<List<Map<String, Object>>> multipleResultSets = this.jdbcService.executeStoredProcedureResultSets(
					"get_careers_result_sets",
					new SqlParameter("$career_one", "Project Manager"),
					new SqlParameter("$career_two", "CTO"));
			this.printMultipleCareers(multipleResultSets);

			// Getting Multiple Result Sets With Row Mappers (Returning Specific Columns)
			final List<List<String>> rowMappers = List.of(
					List.of("name", "age"),
					List.of("name", "dob"));
			multipleResultSets = this.jdbcService.executeStoredProcedureResultSetsWithRowMappers(
					"get_careers_result_sets",
					rowMappers,
					new SqlParameter("$career_one", "Project Manager"),
					new SqlParameter("$career_two", "CTO"));
			this.printMultipleCareersWithRowMappers(multipleResultSets, rowMappers);
		};
	}

	/*
	 * PRIVATE METHODS (UTILITIES)
	 */
	private void printMultipleCareers(List<List<Map<String, Object>>> multipleResultSets) throws SQLException {
		System.out.println("\n[RESULT SETS]\n");
		for (final List<Map<String, Object>> singleResultSet : multipleResultSets) {
			System.out.println("Current result set:");
			for (final Map<String, Object> sqlRow : singleResultSet) {
				printPerson(sqlRow);
			}
			System.out.println("");
		}
	}

	private void printMultipleCareersWithRowMappers(List<List<Map<String, Object>>> multipleResultSets,
			List<List<String>> rowMappers) throws SQLException {
		System.out.println("\n[RESULT SETS WITH ROW MAPPERS]\n");
		int rowMapperIndex = 0;
		for (final List<Map<String, Object>> singleResultSet : multipleResultSets) {
			System.out.println("Current result set:");
			final List<String> rowMapper = rowMappers.get(rowMapperIndex);
			for (final Map<String, Object> sqlRow : singleResultSet) {
				printPerson(sqlRow, rowMapper);
			}
			rowMapperIndex++;
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

	private void printPerson(Map<String, Object> sqlRow, List<String> rowMapper) {
		boolean firstRow = true;
		String rowMessage = "";
		for (final String column : rowMapper) {
			if (firstRow == true) {
				rowMessage += String.format("%s - %s", column, sqlRow.get(column));
				firstRow = false;
			} else {
				rowMessage += String.format(", %s - %s", column, sqlRow.get(column));
			}
		}
		System.out.println(rowMessage);
	}
}
