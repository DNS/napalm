package com.githum.napalm4j.examples.invoicing;

import javax.annotation.Resource;
import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.Database;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;

/**
 * Spring bootstrap
 * 
 * @author jacekf
 * 
 */
@Configuration
public class ApplicationConfiguration {

	@Autowired
	private ApplicationContext ctx;

	@Resource(name = "db")
	private DataSource ds;

	@Bean(name = "emf")
	@Scope("singleton")
	public LocalContainerEntityManagerFactoryBean getEmf() {
		LocalContainerEntityManagerFactoryBean emf = new LocalContainerEntityManagerFactoryBean();
		emf.setPersistenceUnitName("napalm_invoicing");
		emf.setDataSource(ds);

		HibernateJpaVendorAdapter adapter = new HibernateJpaVendorAdapter();
		adapter.setDatabase(Database.H2);
		adapter.setGenerateDdl(true);
		emf.setJpaVendorAdapter(adapter);

		return emf;
	}

	@Bean(name = "transactionManager")
	@Scope("singleton")
	public JpaTransactionManager getTxManager() {
		JpaTransactionManager tx = new JpaTransactionManager();
		tx.setDataSource(ds);

		LocalContainerEntityManagerFactoryBean emf = ctx.getBean(LocalContainerEntityManagerFactoryBean.class);

		tx.setEntityManagerFactory(emf.getNativeEntityManagerFactory());
		return tx;
	}

}
