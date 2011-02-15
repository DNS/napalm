package com.githum.napalm4j.examples.invoicing.modules;

import javax.persistence.EntityManagerFactory;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.jpa.JpaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.github.napalm.freemarker.NapalmFreeMarker;

@Service
@Path("/customer")
@Produces("text/html")
@Transactional
public class CustomerRestService {

	@Autowired
	private NapalmFreeMarker fm;
	private JpaTemplate jpa;
	@Autowired
	public void setEntityManagerFactory(EntityManagerFactory emf) {
		this.jpa = new JpaTemplate(emf);
	}
	@Autowired private DatabasePopulator populator;

	@GET
	public String getIndex() {
		populator.createData();
		return fm.render("customers.ftl", "customers",
				this.jpa.findByNamedQuery("customers"));
	}

}
