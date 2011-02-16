package com.github.napalm4j.jpa;

import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;

import javax.annotation.PostConstruct;
import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.orm.jpa.EntityManagerFactoryInfo;
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
			Map<String, EntityManagerFactoryInfo> emfs = ctx.getBeansOfType(EntityManagerFactoryInfo.class);
			for (Entry<String, EntityManagerFactoryInfo> entry : emfs.entrySet()) {
				if (dataSource.equals(entry.getValue().getDataSource())) {
					JpaTemplate t = new JpaTemplate(entry.getValue().getNativeEntityManagerFactory());
					cachedTemplates.put(dataSource, t);
				}
			}
		}
		return cachedTemplates.get(dataSource);
	}

	/*
	 * (non-Javadoc)
	 * @see com.github.napalm.interfaces.DataProvider#query(java.lang.Object, java.lang.String, java.lang.Object[])
	 */
	@Override
	public CallableOperation<JpaTemplate, Object> query(DataSource dataSource, String queryName, Object... parameters) {
		CallableOperation<JpaTemplate, Object> c = new CallableOperation<JpaTemplate, Object>() {
			@SuppressWarnings("unchecked")
			@Override
			public Object call() throws Exception {
				JpaTemplate t = getDataInterface();
				List<Object> all = null;
				String queryString = queries.get(getName());
				if (queryString != null) {
					// load named query from our YAML files
					all = t.find(queryString, getParameters());
				} else {
					// loading regular JPA named query from @NamedQuery annotation
					all = t.findByNamedQuery(getName(), getValue());
				}

				if (all.size() == 0) {
					return null;
				} else if (all.size() == 1) {
					return all.get(0);
				} else {
					throw new InvalidDataAccessApiUsageException("Expected single result, got " + all.size() + ": [" + all + "]");
				}
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
	public CallableOperation<JpaTemplate, List<Object>> queryForList(DataSource dataSource, String queryName, Object... parameters) {
		CallableOperation<JpaTemplate, List<Object>> c = new CallableOperation<JpaTemplate, List<Object>>() {
			@SuppressWarnings("unchecked")
			@Override
			public List<Object> call() throws Exception {
				JpaTemplate t = getDataInterface();
				String queryString = queries.get(getName());
				if (queryString != null) {
					// load named query from our YAML files
					return t.find(queryString, getParameters());
				} else {
					// loading regular JPA named query from @NamedQuery annotation
					return t.findByNamedQuery(getName(), getValue());
				}
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
