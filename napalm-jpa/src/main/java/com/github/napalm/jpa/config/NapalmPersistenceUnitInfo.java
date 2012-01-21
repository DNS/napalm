package com.github.napalm.jpa.config;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
import java.util.Properties;

import javax.persistence.SharedCacheMode;
import javax.persistence.ValidationMode;
import javax.persistence.spi.ClassTransformer;
import javax.persistence.spi.PersistenceUnitInfo;
import javax.persistence.spi.PersistenceUnitTransactionType;
import javax.sql.DataSource;

import lombok.Getter;

import com.google.common.collect.Lists;

/**
 * Avoids the need for an actual persistence.xml file
 * 
 * @author jacekf
 * 
 */
public class NapalmPersistenceUnitInfo implements PersistenceUnitInfo {

	@Getter
	private String persistenceUnitName;
	@Getter
	private DataSource nonJtaDataSource;
	@Getter
	private Properties properties = new Properties();
	@Getter
	private List<URL> jarFileUrls = Lists.newArrayList();

	public NapalmPersistenceUnitInfo(String name, DataSource ds) {
		this.persistenceUnitName = name;
		this.nonJtaDataSource = ds;
		properties.put("hibernate.archive.autodetection", "class"); //enable auto-discovery in a Java SE environment
	}

	@Override
	public String getPersistenceProviderClassName() {
		return null;
	}

	@Override
	public PersistenceUnitTransactionType getTransactionType() {
		return PersistenceUnitTransactionType.RESOURCE_LOCAL;
	}

	@Override
	public DataSource getJtaDataSource() {
		return null;
	}

	@Override
	public List<String> getMappingFileNames() {
		return null;
	}

	@Override
	public URL getPersistenceUnitRootUrl() {
		try {
			//TODO: fix
			return new URL("file:/home/jacekf/src/napalm/napalm/napalm-jpa/target/test-classes/");
		} catch (MalformedURLException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public List<String> getManagedClassNames() {
		return Lists.newArrayList();
	}

	@Override
	public boolean excludeUnlistedClasses() {
		return false;
	}

	@Override
	public SharedCacheMode getSharedCacheMode() {
		return SharedCacheMode.ENABLE_SELECTIVE;
	}

	@Override
	public ValidationMode getValidationMode() {
		return ValidationMode.AUTO;
	}

	@Override
	public String getPersistenceXMLSchemaVersion() {
		return "1.0";
	}

	@Override
	public ClassLoader getClassLoader() {
		return Thread.currentThread().getContextClassLoader();
	}

	@Override
	public void addTransformer(ClassTransformer transformer) {
		// TODO Auto-generated method stub
	}

	@Override
	public ClassLoader getNewTempClassLoader() {
		return null;
	}

}
