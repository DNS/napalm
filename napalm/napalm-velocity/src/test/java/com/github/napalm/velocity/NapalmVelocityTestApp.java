package com.github.napalm.velocity;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.napalm.Napalm;
import com.google.common.collect.ImmutableMap;

@Service
@Path("/")
public class NapalmVelocityTestApp {

	@Autowired
	private NapalmVelocityEngine velocity;

	@GET
	@Produces(MediaType.TEXT_PLAIN)
	public String hi() {
		return velocity.render("NapalmVelocityTest.vm", ImmutableMap.of("ip", "0.0.0.0"));
	}

	public static void main(String[] args) {
		Napalm.run(8080, NapalmVelocityTestApp.class, NapalmVelocityEngine.class);
	}
}
