package com.github.napalm.test;

import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.springframework.stereotype.Service;

import com.github.napalm.Napalm;

/**
 * Unit test for simple App.
 */
@Service
@Path("/")
@Produces(MediaType.APPLICATION_JSON)
public class NapalmTestApp {

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

	public static void main(String[] args) {
		Napalm.run(8080, NapalmTestApp.class);
	}

}
