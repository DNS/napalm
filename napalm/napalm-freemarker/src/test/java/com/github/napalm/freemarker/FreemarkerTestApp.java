package com.github.napalm.freemarker;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.napalm.Napalm;

@Service
@Path("/")
public class FreemarkerTestApp {

	@Autowired
	private NapalmFreeMarker freemarker;

	@GET()
	@Path("/{user}")
	@Produces(MediaType.TEXT_PLAIN)
	public String hi(@PathParam("user") String user) {
		return freemarker.render("index.txt", "user", user, "ip", "0.0.0.0");
	}

	public static void main(String[] args) {
		Napalm.run(8080, FreemarkerTestApp.class, NapalmFreeMarker.class);
	}
}
