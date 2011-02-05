package com.github.napalm4j.sql.query;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.ConcurrentHashMap;

import javax.annotation.PostConstruct;
import javax.sql.DataSource;

import org.javatuples.Pair;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import com.github.napalm.spring.query.CallableAdaptor;
import com.github.napalm.spring.query.QueryEngine;
import com.github.napalm.utils.QueryUtils;

/**
 * Napalm SQL Query engine
 * 
 * @author jacekf
 * 
 */
public class SqlQueryEngine implements QueryEngine<DataSource, Map<String, Object>> {

	private ConcurrentHashMap<DataSource, JdbcTemplate> cachedTemplates = new ConcurrentHashMap<DataSource, JdbcTemplate>();
	private ConcurrentHashMap<String, String> queries = new ConcurrentHashMap<String, String>();

	@PostConstruct
	public void init() {
		// find all the user defined queries
		queries.putAll(QueryUtils.parseQueries("sql"));
	}

	private JdbcTemplate getTemplate(DataSource dataSource) {
		if (!cachedTemplates.containsKey(dataSource)) {
			JdbcTemplate t = new JdbcTemplate(dataSource);
			cachedTemplates.put(dataSource, t);
		}
		return cachedTemplates.get(dataSource);
	}

	/*
	 * (non-Javadoc)
	 * @see com.github.napalm.spring.query.QueryEngine#query(java.lang.Object, java.lang.String, java.lang.Object[])
	 */
	@Override
	public Pair<String, Callable<Map<String, Object>>> query(DataSource dataSource, String queryName, Object... parameters) {
		CallableAdaptor<JdbcTemplate, Map<String, Object>> c = new CallableAdaptor<JdbcTemplate, Map<String, Object>>() {
			@Override
			public Map<String, Object> call() throws Exception {
				return getDataInterface().queryForMap(getQueryValue(), getParameters());
			}
		};
		// avoids using final variables
		c.setDataInterface(getTemplate(dataSource));
		c.setQueryValue(queries.get(queryName));

		return new Pair<String, Callable<Map<String, Object>>>(queryName, c);
	}

	@Override
	public Pair<String, Callable<List<Map<String, Object>>>> queryForList(DataSource dataSource, String queryName,
			Object... parameters) {
		CallableAdaptor<JdbcTemplate, List<Map<String, Object>>> c = new CallableAdaptor<JdbcTemplate, List<Map<String, Object>>>() {
			@Override
			public List<Map<String, Object>> call() throws Exception {
				RowMapper<Map<String, Object>> rm = new RowMapper<Map<String, Object>>() {
					@Override
					public Map<String, Object> mapRow(ResultSet rs, int rowNum) throws SQLException {
						return null;
					}
				};
				return null;
			}
		};
		// avoids using final variables
		c.setDataInterface(getTemplate(dataSource));
		c.setQueryValue(queries.get(queryName));

		return new Pair<String, Callable<List<Map<String, Object>>>>(queryName, c);
	}

}
