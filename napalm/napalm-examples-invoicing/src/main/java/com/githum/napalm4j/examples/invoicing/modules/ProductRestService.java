package com.githum.napalm4j.examples.invoicing.modules;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.napalm.freemarker.NapalmFreeMarker;

@Service @Path("/product") @Produces("text/html")
public class ProductRestService {

	@Autowired private NapalmFreeMarker fm;
	
	@GET
	public String getIndex() {
		return fm.render("products.ftl");
	}
	
}
