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
 * App where the root / is served via static servlet
 */
@Service
@Path("/services")
@Produces(MediaType.APPLICATION_JSON)
public class NapalmStaticTestApp {

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
		Napalm.addResource("db", "jdbc:h2:mem:db2");
		Napalm.run(8081, NapalmStaticTestApp.class);
	}
}
