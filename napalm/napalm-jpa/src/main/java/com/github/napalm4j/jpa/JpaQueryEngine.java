package com.github.napalm4j.jpa;

import java.util.List;

import javax.sql.DataSource;

import lombok.Getter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.orm.jpa.JpaTemplate;

import com.github.napalm.interfaces.CallableOperation;
import com.github.napalm.interfaces.DataProvider;

/**
 * Main Napalm JPA query engine
 * 
 * Allows to parallelize JPA queries and fetch named queries from classpath:jpa/*.yml (vs using regular JpaTemplate)
 */
public class JpaQueryEngine implements DataProvider<DataSource, JpaTemplate, Object> {

	@Getter
	private JpaTemplate template;

	public void setJpaTemplate(JpaTemplate t) {
		this.template = t;
	}

	@Autowired
	private JpaNamedQueryManager queryManager;

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
				if (getValue() != null) {
					// load named query from our YAML files
					all = t.find(getValue(), getParameters());
				} else {
					// loading regular JPA named query from @NamedQuery annotation
					all = t.findByNamedQuery(getName(), getParameters());
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
		c.setDataInterface(this.template);
		
		String queryString = queryManager.getQuery(queryName);
		if (queryString != null) {
			c.setValue(queryString);
		} else {
			c.setValue(null);
		}
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
				if (getValue() != null) {
					// load named query from our YAML files
					return t.find(getValue(), getParameters());
				} else {
					// loading regular JPA named query from @NamedQuery annotation
					return t.findByNamedQuery(getName(), getValue());
				}
			}
		};
		// avoids using final variables
		c.setName(queryName);
		c.setDataInterface(this.template);
		String queryString = queryManager.getQuery(queryName);
		if (queryString != null) {
			c.setValue(queryString);
		} else {
			c.setValue(null);
		}
		c.setParameters(parameters);

		return c;
	}
}
