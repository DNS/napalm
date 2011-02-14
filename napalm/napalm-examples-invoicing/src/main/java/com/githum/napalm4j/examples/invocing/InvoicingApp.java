package com.githum.napalm4j.examples.invocing;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.napalm.Napalm;
import com.github.napalm.freemarker.NapalmFreeMarker;

/**
 * Invocing app
 *
 */
@Service @Path("/") @Produces("text/html")
public class InvoicingApp 
{
	@Autowired private NapalmFreeMarker fm;
	
	@GET
	public String getIndex() {
		return fm.render("index.html");
	}
	
    public static void main( String[] args )
    {
        Napalm.run(8080, InvoicingApp.class,NapalmFreeMarker.class);
    }
}
