package com.github.napalm.test.jpa.app;

import java.util.List;

import javax.ws.rs.DELETE;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

import junit.framework.TestCase;

import org.springframework.stereotype.Service;

import com.github.napalm.Napalm;
import com.github.napalm.jpa.NapalmJpa;

/**
 * JPA test app
 */
@Service
@Path("/")
public class NapalmJpaTestApp extends TestCase {

	// @Autowired
	// private EntityManagerFactory emf;

	// @Autowired
	// private NapalmJpa query;

	@GET
	public List<Customer> getCustomers() {
		return null;
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
		Napalm.addResource("db", "jdbc:h2:mem:db");
		Napalm.run(8080, NapalmJpaTestApp.class, NapalmJpa.class);
	}

}
