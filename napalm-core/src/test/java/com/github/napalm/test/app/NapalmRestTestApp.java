package com.github.napalm.test.app;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;
import javax.sql.DataSource;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import com.github.napalm.Napalm;

/**
 * Test app where the root is server via JAX-RS and static resources are from the /static context
 */
@Service
@Path("/")
@Produces(MediaType.APPLICATION_JSON)
public class NapalmRestTestApp {

	@Resource(name = "db")
	private DataSource db;

	@GET
	public String get() {
		return "hi";
	}

	@GET
	@Path("/user")
	public List<NapalmTestUser> getUsers() {
		List<NapalmTestUser> users = new ArrayList<NapalmTestUser>();
		for (int i = 0; i < 3; i++) {
			NapalmTestUser user = new NapalmTestUser();
			user.setId(i);
			user.setName("User " + i);
			users.add(user);
		}
		return users;
	}

	@GET
	@Path("/db")
	@Produces(MediaType.TEXT_PLAIN)
	public String getDbInfo() {
		return String.valueOf(new JdbcTemplate(db).queryForInt("SELECT COUNT(*) FROM INFORMATION_SCHEMA.CATALOGS"));
	}

	public static void main(String[] args) {
		Napalm.run("src/test/resources/config/restapp.yml", NapalmRestTestApp.class);
	}
}
