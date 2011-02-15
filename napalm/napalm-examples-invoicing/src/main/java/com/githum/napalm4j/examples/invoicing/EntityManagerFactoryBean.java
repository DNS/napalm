package com.githum.napalm4j.examples.invoicing;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.sql.DataSource;

import org.springframework.instrument.classloading.InstrumentationLoadTimeWeaver;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.Database;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.stereotype.Service;

/**
 * Bootstraps JPA into the Spring context
 */
@Service("entityManagerFactory")
public class EntityManagerFactoryBean extends LocalContainerEntityManagerFactoryBean {

	@Resource(name="db")
	private DataSource ds;
	
	@PostConstruct
	public void init() {
		setPersistenceUnitName("napalm_invoicing");
		setDataSource(ds);
		
		HibernateJpaVendorAdapter adapter = new HibernateJpaVendorAdapter();
		adapter.setDatabase(Database.H2);
		adapter.setGenerateDdl(true);
		setLoadTimeWeaver(new InstrumentationLoadTimeWeaver());
		
		setJpaVendorAdapter(adapter);
	}
	
}
