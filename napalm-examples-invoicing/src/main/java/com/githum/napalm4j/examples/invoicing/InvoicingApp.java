package com.githum.napalm4j.examples.invoicing;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.napalm.Napalm;
import com.github.napalm.freemarker.NapalmFreeMarker;

/**
 * Invoicing app
 */
@Service
@Path("/")
@Produces("text/html")
public class InvoicingApp {
	@Autowired
	private NapalmFreeMarker fm;

	@GET
	public String getIndex() {
		return fm.render("index.ftl");
	}

	public static void main(String[] args) {
		Napalm.run("src/test/resources/config/app.yml", InvoicingApp.class, NapalmFreeMarker.class);
	}
}
