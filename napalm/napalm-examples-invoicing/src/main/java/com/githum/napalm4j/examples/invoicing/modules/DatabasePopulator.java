package com.githum.napalm4j.examples.invoicing.modules;

import javax.annotation.PostConstruct;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.PersistenceException;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.jpa.JpaCallback;
import org.springframework.orm.jpa.JpaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.github.napalm.freemarker.NapalmFreeMarker;
import com.githum.napalm4j.examples.invoicing.model.Customer;

@Service
@Transactional
public class DatabasePopulator {

	private JpaTemplate jpa;

	@Autowired
	public void setEntityManagerFactory(EntityManagerFactory emf) {
		this.jpa = new JpaTemplate(emf);
	}

	@Transactional
	public void createData() {

		for (int i = 0; i < 100; i++) {
			Customer c = new Customer();
			c.setFirstName("John");
			c.setLastName("Last Name #" + i);
			c.setEmail("john" + i + "@test.com");
			c.setState("NY");
			jpa.persist(c);
		}
	}


}
