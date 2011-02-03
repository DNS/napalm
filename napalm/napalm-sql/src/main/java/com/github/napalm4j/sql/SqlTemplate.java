package com.github.napalm4j.sql;

import javax.sql.DataSource;

import org.springframework.jdbc.core.JdbcTemplate;

/**
 * Main template for executing SQL queries stored in an external YAML file
 * 
 * @author jacekf
 */
public class SqlTemplate {

	private JdbcTemplate jdbc = null;

	public SqlTemplate(DataSource ds) {
		this.jdbc = new JdbcTemplate(ds);
	}

}
