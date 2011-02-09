package com.github.napalm4j.sql.query;

import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.ConcurrentHashMap;

import javax.annotation.PostConstruct;
import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.stereotype.Service;

import com.github.napalm.interfaces.CallableOperation;
import com.github.napalm.interfaces.DataProvider;
import com.github.napalm.spring.NapalmConfig;
import com.github.napalm.utils.QueryUtils;

/**
 * Napalm SQL Query engine
 * 
 * @author jacekf
 * 
 */
@Service
public class SqlQueryEngine implements DataProvider<DataSource, Map<String, Object>> {

	@Autowired
	private NapalmConfig config;

	@Autowired
	private TaskScheduler scheduler;

	private ConcurrentHashMap<DataSource, JdbcTemplate> cachedTemplates = new ConcurrentHashMap<DataSource, JdbcTemplate>();
	private ConcurrentHashMap<String, String> queries = new ConcurrentHashMap<String, String>();

	@PostConstruct
	public void init() {
		// find all the user defined queries
		queries.putAll(QueryUtils.parseQueries("sql"));

		// if in dev mode, reload every second in the background
		if (config.isInDevelopmentMode()) {
			scheduler.scheduleAtFixedRate(new Runnable() {
				@Override
				public void run() {
					queries.putAll(QueryUtils.parseQueries("sql"));
				}
			}, 1000);
		}
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
	public Callable<Map<String, Object>> query(DataSource dataSource, String queryName, Object... parameters) {
		CallableOperation<JdbcTemplate, Map<String, Object>> c = new CallableOperation<JdbcTemplate, Map<String, Object>>() {
			@Override
			public Map<String, Object> call() throws Exception {
				return getDataInterface().queryForMap(getValue(), getParameters());
			}
		};
		// avoids using final variables
		c.setName(queryName);
		c.setDataInterface(getTemplate(dataSource));
		c.setValue(queries.get(queryName));
		c.setParameters(parameters);

		return c;
	}

	/*
	 * (non-Javadoc)
	 * @see com.github.napalm.interfaces.DataProvider#queryForList(java.lang.Object, java.lang.String, java.lang.Object[])
	 */
	@Override
	public Callable<List<Map<String, Object>>> queryForList(DataSource dataSource, String queryName, Object... parameters) {
		CallableOperation<JdbcTemplate, List<Map<String, Object>>> c = new CallableOperation<JdbcTemplate, List<Map<String, Object>>>() {
			@Override
			public List<Map<String, Object>> call() throws Exception {
				return getDataInterface().queryForList(getValue(), getParameters());
			}
		};
		// avoids using final variables
		c.setName(queryName);
		c.setDataInterface(getTemplate(dataSource));
		c.setValue(queries.get(queryName));
		c.setParameters(parameters);

		return c;
	}

}
