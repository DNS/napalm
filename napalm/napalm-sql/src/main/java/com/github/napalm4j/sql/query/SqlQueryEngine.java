package com.github.napalm4j.sql.query;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ConcurrentHashMap;

import javax.annotation.PostConstruct;
import javax.sql.DataSource;

import org.javatuples.Pair;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;

import com.github.napalm.spring.query.CallableAdaptor;
import com.github.napalm.spring.query.QueryEngine;
import com.github.napalm.utils.QueryUtils;

/**
 * Napalm SQL Query engine
 * 
 * @author jacekf
 * 
 */
public class SqlQueryEngine implements QueryEngine<DataSource> {

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
	public <T> Pair<String, Callable<T>> query(final DataSource dataSource, String queryName, Object... parameters) {
		CallableAdaptor<JdbcTemplate, T> c = new CallableAdaptor<JdbcTemplate, T>() {
			@Override
			public T call() throws Exception {
				ResultSetExtractor<T> r = new ResultSetExtractor<T>() {
					@Override
					public T extractData(ResultSet rs) throws SQLException, DataAccessException {
						// TODO Auto-generated method stub
						return null;
					}
				};
				return getDataInterface().query(getQueryValue(), getParameters(), r);
			}
		};
		// avoids using final variables
		c.setDataInterface(getTemplate(dataSource));
		c.setQueryValue(queries.get(queryName));

		return new Pair<String, Callable<T>>(queryName, c);
	}

	/*
	 * (non-Javadoc)
	 * @see com.github.napalm.spring.query.QueryEngine#queryForList(java.lang.Object, java.lang.String, java.lang.Object[])
	 */
	@Override
	public <T> Pair<String, Callable<List<T>>> queryForList(DataSource dataSource, String queryName, Object... parameters) {
		// TODO Auto-generated method stub
		return null;
	}

}
