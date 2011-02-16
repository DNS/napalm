package com.github.napalm4j.jpa;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

import javax.annotation.PostConstruct;
import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.orm.jpa.JpaTemplate;
import org.springframework.scheduling.TaskScheduler;

import com.github.napalm.interfaces.CallableOperation;
import com.github.napalm.interfaces.DataProvider;
import com.github.napalm.spring.NapalmConfig;
import com.github.napalm.utils.QueryUtils;

/**
 * Main Napalm JPA query engine
 * 
 * Allows to parallelize JPA queries and fetch named queries from classpath:jpa/*.yml (vs using regular JpaTemplate)
 */
public class JpaQueryEngine implements DataProvider<DataSource, JpaTemplate, Object> {

	@Autowired
	private NapalmConfig config;
	@Autowired
	private ApplicationContext ctx;

	@Autowired
	private TaskScheduler scheduler;

	private ConcurrentHashMap<DataSource, JpaTemplate> cachedTemplates = new ConcurrentHashMap<DataSource, JpaTemplate>();
	private ConcurrentHashMap<String, String> queries = new ConcurrentHashMap<String, String>();

	@PostConstruct
	public void init() {
		// find all the user defined queries
		queries.putAll(QueryUtils.parseQueries("jpa"));

		// if in dev mode, reload every second in the background
		if (config.isInDevelopmentMode()) {
			scheduler.scheduleAtFixedRate(new Runnable() {
				@Override
				public void run() {
					queries.putAll(QueryUtils.parseQueries("jpa"));
				}
			}, 1000);
		}
	}

	private JpaTemplate getTemplate(DataSource dataSource) {
		if (!cachedTemplates.containsKey(dataSource)) {
			// TODO ctx.getBeansOfType(EntityManagerFactory.class);

			JpaTemplate t = null;// TODO = new JpaTemplate(dataSource);
			cachedTemplates.put(dataSource, t);
		}
		return cachedTemplates.get(dataSource);
	}

	/*
	 * (non-Javadoc)
	 * @see com.github.napalm.interfaces.DataProvider#query(java.lang.Object, java.lang.String, java.lang.Object[])
	 */
	@Override
	public CallableOperation<JpaTemplate, Object> query(DataSource dataSource, String queryName, Object... parameters) {
		// TODO Auto-generated method stub
		return null;
	}

	/*
	 * (non-Javadoc)
	 * @see com.github.napalm.interfaces.DataProvider#queryForList(java.lang.Object, java.lang.String, java.lang.Object[])
	 */
	@Override
	public CallableOperation<JpaTemplate, List<Object>> queryForList(DataSource dataSource, String queryName, Object... parameters) {
		// TODO Auto-generated method stub
		return null;
	}

}
