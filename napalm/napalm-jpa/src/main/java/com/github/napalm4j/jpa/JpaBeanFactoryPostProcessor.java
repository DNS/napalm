package com.github.napalm4j.jpa;

import java.util.Map;
import java.util.Map.Entry;

import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.orm.jpa.EntityManagerFactoryInfo;
import org.springframework.orm.jpa.JpaTemplate;

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
public class JpaBeanFactoryPostProcessor implements BeanFactoryPostProcessor {

	private static final Logger LOG = LoggerFactory.getLogger(JpaBeanFactoryPostProcessor.class);

	/*
	 * (non-Javadoc)
	 * @see
	 * org.springframework.beans.factory.config.BeanFactoryPostProcessor#postProcessBeanFactory(org.springframework.beans.factory
	 * .config.ConfigurableListableBeanFactory)
	 */
	@Override
	public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {

		Map<String, DataSource> dses = beanFactory.getBeansOfType(DataSource.class);
		Map<String, EntityManagerFactoryInfo> emfs = beanFactory.getBeansOfType(EntityManagerFactoryInfo.class);
		Map<String, JpaTemplate> templates = beanFactory.getBeansOfType(JpaTemplate.class);
		Map<String, JpaQueryEngine> engines = beanFactory.getBeansOfType(JpaQueryEngine.class);

		for (Entry<String, DataSource> entry : dses.entrySet()) {
			EntityManagerFactoryInfo emf = findEmfInfo(entry.getValue(), emfs);
			// auto-create if not defined
			if (emf == null) {

			}
			// init EMF

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

}
