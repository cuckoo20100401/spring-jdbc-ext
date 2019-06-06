package org.cuckoo.springframework.jdbc.ext;

import org.springframework.jdbc.datasource.DriverManagerDataSource;

public class DBUtils {

	public static JdbcTemplateExt getJdbcTemplateExt() {
		
		DriverManagerDataSource dataSource = new DriverManagerDataSource();
		dataSource.setDriverClassName("org.postgresql.Driver");
		dataSource.setUrl("jdbc:postgresql://localhost:5432/example");
		dataSource.setUsername("postgres");
		dataSource.setPassword("postgres");
		
		JdbcTemplateExt extJdbcTemplate = new JdbcTemplateExt(dataSource);
		return extJdbcTemplate;
	}
}
