package com.github.napalm.spring;

import java.util.concurrent.Callable;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Common handler for all template plugins Take care of sanitizing error messages, etc.
 * 
 * @author jacekf
 * 
 */
@Service
public class TemplateHandler {

	private static final Logger LOG = LoggerFactory.getLogger(TemplateHandler.class);

	@Autowired
	private NapalmConfig config;

	/**
	 * Renders the template logic and sanitizes the error to avoid showing internal details
	 * 
	 * @param t Closure (I wish)
	 * @return String
	 */
	public String render(Callable<String> t) {
		try {
			return t.call();
		} catch (Exception ex) {
			if (config.isInDevelopmentMode()) {
				System.out.println(ex.getMessage());
			}
			LOG.error(ex.getMessage(), ex);
			throw new WebApplicationException(Response.serverError()
					.entity("Internal server template error. See server log for details.").build());
		}
	}

}
