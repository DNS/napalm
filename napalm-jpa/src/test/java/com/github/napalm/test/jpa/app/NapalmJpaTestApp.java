package com.github.napalm.test.jpa.app;

import java.util.List;

import javax.ws.rs.DELETE;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

import junit.framework.TestCase;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.jpa.JpaTemplate;
import org.springframework.stereotype.Service;

import com.github.napalm.Napalm;
import com.github.napalm.jpa.NapalmJpa;
import com.google.common.collect.Lists;

/**
 * JPA test app
 */
@Service
@Path("/")
@Produces(MediaType.APPLICATION_JSON)
public class NapalmJpaTestApp extends TestCase {

	@Autowired
	private JpaTemplate jpa;

	@Autowired
	private NapalmJpa query;

	@SuppressWarnings("unchecked")
	@GET
	public List<Customer> getCustomers() {
		// return jpa.find("SELECT c FROM Customer c");

		Customer c = new Customer();
		c.setEmail("jacek@test.com");
		c.setId(1L);
		c.setFirstName("Jacek");
		c.setLastName("Furmankiewicz");

		return Lists.newArrayList(c);

	}

	@GET
	@Path("/{id}")
	public Customer getCustomer(@PathParam("id") Long id) {
		return null;
	}

	@POST
	public Response addCustomer(@FormParam("firstName") String firstName, @FormParam("lastName") String lastName,
			@FormParam("email") String email, @Context UriInfo info) {
		/*
		 * EntityManager em = emf.createEntityManager(); Customer c = new Customer(); c.setFirstName(firstName);
		 * c.setLastName(lastName); c.setEmail(email); em.persist(c); return
		 * Response.created(info.getRequestUriBuilder().build(c.getId())).build();
		 */
		return null;
	}

	@PUT
	public Response updateCustomer(@FormParam("firstName") String firstName, @FormParam("lastName") String lastName,
			@FormParam("email") String email) {
		return null;
	}

	@DELETE
	@Path("/{id}")
	public Response deleteCustomer(@PathParam("id") Long id) {
		return null;
	}

	@DELETE
	public Response deleteAllCustomers() {
		return null;
	}

	public static void main(String[] args) {
		Napalm.run("src/test/resources/config/app.yml", NapalmJpaTestApp.class, NapalmJpa.class);
	}

}
