package com.github.napalm.test.velocity;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.napalm.Napalm;
import com.github.napalm.velocity.NapalmVelocity;

@Service
@Path("/")
public class VelocityTestApp {

	@Autowired
	private NapalmVelocity velocity;

	@GET()
	@Path("/{user}")
	@Produces(MediaType.TEXT_PLAIN)
	public String hi(@PathParam("user") String user) {
		return velocity.render("NapalmVelocityTest.vm", "user", user, "ip", "0.0.0.0");
	}

	public static void main(String[] args) {
		Napalm.run(null, VelocityTestApp.class, NapalmVelocity.class);
	}
}
