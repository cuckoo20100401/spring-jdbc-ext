package org.cuckoo.springframework.jdbc.ext;

import org.springframework.jdbc.datasource.DriverManagerDataSource;

public class DBUtils {

	public static ExtJdbcTemplate getExtJdbcTemplate() {
		
		DriverManagerDataSource dataSource = new DriverManagerDataSource();
		dataSource.setDriverClassName("org.postgresql.Driver");
		dataSource.setUrl("jdbc:postgresql://localhost:5432/example");
		dataSource.setUsername("postgres");
		dataSource.setPassword("postgres");
		
		ExtJdbcTemplate extJdbcTemplate = new ExtJdbcTemplate(dataSource);
		return extJdbcTemplate;
	}
}
