package com.github.napalm.jpa;

import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;

import org.eclipse.jetty.servlet.FilterHolder;
import org.eclipse.jetty.servlet.FilterMapping;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.orm.jpa.EntityManagerFactoryInfo;
import org.springframework.orm.jpa.JpaTemplate;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.support.OpenEntityManagerInViewFilter;
import org.springframework.orm.jpa.vendor.Database;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.stereotype.Service;

import com.github.napalm.spring.NapalmConfig;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;

/**
 * Auto-creates (if requored) all the objects required for easy JPA usage based on JDBC DataSources in the context:<br/>
 * * EntityManagerFactory (JPA)<br/>
 * * JpaTransactionManager (Spring)<br/>
 * * JpaTemplate (Spring)<br/>
 * * JpaQueryEngine (Napalm)<br/>
 * 
 * @author jacekf
 * 
 */
@Service
public class JpaBeanFactoryPostProcessor implements BeanFactoryPostProcessor {

	private static final Logger LOG = LoggerFactory.getLogger(JpaBeanFactoryPostProcessor.class);

	public static final String EMF_SUFFIX = "Emf";
	public static final String JPA_TEMPLATE_SUFFIX = "JpaTemplate";
	public static final String JPA_TX_MANAGER_SUFFIX = "JpaTransactionManager";
	public static final String JPA_QUERY_ENGINE_SUFFIX = "Jpa";

	private static final Map<String, Database> jdbc2Db = new ImmutableMap.Builder<String, Database>().put("db2", Database.DB2)
			.put("derby", Database.DERBY).put("h2", Database.H2).put("hsql", Database.HSQL).put("informix", Database.INFORMIX)
			.put("mysql", Database.MYSQL).put("oracle", Database.ORACLE).put("postgresql", Database.POSTGRESQL)
			.put("sqlserver", Database.SQL_SERVER).put("jtds", Database.SQL_SERVER).put("sybase", Database.SYBASE).build();

	private static final Set<String> memoryDb = new ImmutableSet.Builder<String>().add("jdbc:h2:mem", "jdbc:derby:memory").build();

	@Autowired
	private ServletContextHandler servletContextHandler;
	@Autowired(required = true)
	private NapalmConfig config;

	/*
	 * (non-Javadoc)
	 * @see
	 * org.springframework.beans.factory.config.BeanFactoryPostProcessor#postProcessBeanFactory(org.springframework.beans.factory
	 * .config.ConfigurableListableBeanFactory)
	 */
	@Override
	public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {

		try {
			Map<String, DataSource> dses = beanFactory.getBeansOfType(DataSource.class);
			if (beanFactory.getParentBeanFactory() != null) {
				dses.putAll(((ConfigurableListableBeanFactory) beanFactory.getParentBeanFactory()).getBeansOfType(DataSource.class));
			}

			Map<String, EntityManagerFactoryInfo> emfs = beanFactory.getBeansOfType(EntityManagerFactoryInfo.class);
			Map<String, JpaTemplate> templates = beanFactory.getBeansOfType(JpaTemplate.class);
			Map<String, JpaTransactionManager> txManagers = beanFactory.getBeansOfType(JpaTransactionManager.class);

			for (Entry<String, DataSource> entry : dses.entrySet()) {
				DataSource ds = entry.getValue();
				String url = ds.getConnection().getMetaData().getURL();
				EntityManagerFactoryInfo emf = findEmfInfo(entry.getValue(), emfs);
				// auto-create if not defined
				if (emf == null) {
					LocalContainerEntityManagerFactoryBean springEmf = new LocalContainerEntityManagerFactoryBean();
					springEmf.setPersistenceUnitName(entry.getKey());
					springEmf.setDataSource(ds);
					springEmf.setBeanName(entry.getKey() + EMF_SUFFIX);

					HibernateJpaVendorAdapter adapter = new HibernateJpaVendorAdapter();
					adapter.setDatabase(getDialect(url));

					// generate DDL by default for in-memory DBs only - assume it's for testing purposes only
					if (isInMemoryDb(url)) {
						adapter.setGenerateDdl(true);
					}
					springEmf.setJpaVendorAdapter(adapter);
					emf = springEmf;
					springEmf.afterPropertiesSet();

					LOG.debug("Auto-creating LocalContainerEntityManagerFactoryBean for DataSource {}", entry.getKey());
					String name = entry.getKey() + EMF_SUFFIX;
					beanFactory.registerSingleton(name, emf);

					// auto-register the OpenEntityManagerInViewFilter filter
					// TODO: make it more intelligent in case of multiple EMFs
					Set<String> restUrls = beanFactory.getBean(NapalmConfig.class).getRestUrls();
					FilterHolder filterHolder = new FilterHolder(OpenEntityManagerInViewFilter.class);
					filterHolder.setInitParameter("entityManagerFactoryBeanName", name); // bind it to a particular EMF

					for (String restUrl : restUrls) {
						if (restUrl.endsWith("/")) {
							servletContextHandler.addFilter(filterHolder, url + "*", FilterMapping.REQUEST);
						} else {
							servletContextHandler.addFilter(filterHolder, url + "/*", FilterMapping.REQUEST);
						}
					}
				}

				// auto-create JpaTransactionManager if not defined
				JpaTransactionManager tx = findTxManager(emf.getNativeEntityManagerFactory(), txManagers);
				if (tx == null) {
					tx = new JpaTransactionManager(emf.getNativeEntityManagerFactory());
					LOG.debug("Auto-creating JpaTransactionManager for DataSource {}", entry.getKey());
					beanFactory.registerSingleton(entry.getKey() + JPA_TX_MANAGER_SUFFIX, tx);
				}

				// auto-create JpaTemplate if not defined
				JpaTemplate jpa = findJpaTemplate(emf.getNativeEntityManagerFactory(), templates);
				if (jpa == null) {
					jpa = new JpaTemplate(emf.getNativeEntityManagerFactory());
					LOG.debug("Auto-creating JpaTemplate for DataSource {}", entry.getKey());
					beanFactory.registerSingleton(entry.getKey() + JPA_TEMPLATE_SUFFIX, jpa);

					// auto-create the JpaQueryEngine for this template
					NapalmJpa engine = new NapalmJpa();
					engine.setTemplate(jpa);
					LOG.debug("Auto-creating JpaQueryEngine for DataSource {}", entry.getKey());
					beanFactory.registerSingleton(entry.getKey() + JPA_QUERY_ENGINE_SUFFIX, engine);
				}

			}
		} catch (RuntimeException ex) {
			throw ex;
		} catch (Exception ex) {
			throw new RuntimeException(ex);
		}
	}

	private EntityManagerFactoryInfo findEmfInfo(DataSource ds, Map<String, EntityManagerFactoryInfo> emfs) {
		for (Entry<String, EntityManagerFactoryInfo> entry : emfs.entrySet()) {
			if (ds.equals(entry.getValue().getDataSource())) {
				return entry.getValue();
			}
		}
		return null;
	}

	private JpaTemplate findJpaTemplate(EntityManagerFactory emf, Map<String, JpaTemplate> templates) {
		for (Entry<String, JpaTemplate> entry : templates.entrySet()) {
			if (emf.equals(entry.getValue().getEntityManagerFactory())) {
				return entry.getValue();
			}
		}
		return null;
	}

	private JpaTransactionManager findTxManager(EntityManagerFactory emf, Map<String, JpaTransactionManager> mgrs) {
		for (Entry<String, JpaTransactionManager> entry : mgrs.entrySet()) {
			if (emf.equals(entry.getValue().getEntityManagerFactory())) {
				return entry.getValue();
			}
		}
		return null;
	}

	// figures out the JpaDialect from the connection URL
	private Database getDialect(String url) {
		for (Entry<String, Database> entry : jdbc2Db.entrySet()) {
			if (url.startsWith("jdbc:" + entry.getKey())) {
				return entry.getValue();
			}
		}
		// did not find
		throw new RuntimeException("Unable to figure out JPA dialect for JDBC connection URL: " + url);
	}

	private boolean isInMemoryDb(String url) {
		for (String prefix : memoryDb) {
			if (url.startsWith(prefix)) {
				return true;
			}
		}
		return false;
	}
}
