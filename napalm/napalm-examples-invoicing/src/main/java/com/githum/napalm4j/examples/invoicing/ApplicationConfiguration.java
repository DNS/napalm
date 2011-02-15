package com.githum.napalm4j.examples.invoicing;

import javax.annotation.Resource;
import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportResource;
import org.springframework.context.annotation.Scope;
import org.springframework.orm.jpa.JpaTransactionManager;

/**
 * Spring bootstrap
 * @author jacekf
 *
 */
@Configuration
@ImportResource("META-INF/invoicingBeans.xml")
public class ApplicationConfiguration {

	@Resource(name="db")
	private DataSource ds;
	
	@Autowired private EntityManagerFactoryBean emf;

	@Bean(name = "txManager")
	@Scope("singleton")
	public JpaTransactionManager getTxManager() {
		JpaTransactionManager tx = new JpaTransactionManager();
		tx.setDataSource(ds);
		tx.setEntityManagerFactory(emf.getNativeEntityManagerFactory());
		return tx;
	}
	
	
}
