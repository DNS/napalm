package com.github.napalm.jpa.config;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.persistence.spi.PersistenceUnitInfo;
import javax.sql.DataSource;

import org.springframework.beans.factory.BeanFactory;
import org.springframework.orm.jpa.persistenceunit.PersistenceUnitManager;

/**
 * Needed to avoid the need for an explicit persistence.xml
 * 
 * @author jacekf
 * 
 */
public class NapalmPersistenceUnitManager implements PersistenceUnitManager {

	public final String DEFAULT_NAME = "db";
	private final Map<String, PersistenceUnitInfo> persistenceUnitInfos = new ConcurrentHashMap<String, PersistenceUnitInfo>();

	private BeanFactory bf;

	public NapalmPersistenceUnitManager(BeanFactory ctx) {
		this.bf = ctx;
	}

	@Override
	public PersistenceUnitInfo obtainDefaultPersistenceUnitInfo() throws IllegalStateException {
		return obtainPersistenceUnitInfo(DEFAULT_NAME);
	}

	@Override
	public PersistenceUnitInfo obtainPersistenceUnitInfo(String persistenceUnitName) throws IllegalArgumentException,
			IllegalStateException {
		if (!persistenceUnitInfos.containsKey(persistenceUnitName)) {
			// find the DataSource by the same name
			DataSource ds = (DataSource) bf.getBean(persistenceUnitName);
			persistenceUnitInfos.put(persistenceUnitName, new NapalmPersistenceUnitInfo(persistenceUnitName, ds));
		}
		return persistenceUnitInfos.get(persistenceUnitName);
	}

}
