package com.github.napalm.interfaces;

import org.eclipse.jetty.servlet.ServletContextHandler;

/**
 * Interface that allows contributing to a servlet set up at run-time (e.g. to add a JPA OpenEntityManagerInViewFilter filter to the
 * jersey servlet)
 * 
 * @author jacekf
 * 
 */
public interface ServletContextHandlerContributor {

	/**
	 * Allows to contribute initial setup to the Jetty ServletContextHandler
	 * 
	 * @param ctx
	 */
	public void contribute(ServletContextHandler ctx);

}