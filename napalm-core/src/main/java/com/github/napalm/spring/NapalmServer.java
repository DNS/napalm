package com.github.napalm.spring;

import java.util.Set;

import javax.ws.rs.Path;

import org.eclipse.jetty.server.Connector;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.nio.SelectChannelConnector;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.context.WebApplicationContext;

import com.google.common.base.Joiner;
import com.google.common.collect.Sets;
import com.sun.jersey.spi.spring.container.servlet.SpringServlet;

@Service
public class NapalmServer extends Server {

	private static final Logger LOG = LoggerFactory.getLogger(NapalmServer.class);

	@Autowired
	private WebApplicationContext ctx;
	@Autowired
	private NapalmConfig config;
	@Autowired
	private ServletContextHandler servletContextHandler;
	
	public void init(int port, Class<?>... apps) {

		Connector connector = new SelectChannelConnector();
		connector.setPort(port);
		setConnectors(new Connector[] { connector });

		servletContextHandler.setContextPath("/");
		servletContextHandler.setAttribute(WebApplicationContext.ROOT_WEB_APPLICATION_CONTEXT_ATTRIBUTE, ctx);

		Set<String> rootRestUrls = getRootRestUrls(apps);

		// Static servlet - either servers from root or /static folder, depending if a REST service is already hooked up to the "/"
		// root
		NapalmStaticServlet staticServlet = new NapalmStaticServlet();
		ServletHolder staticHolder = new ServletHolder(staticServlet);

		// Jersey servlet
		SpringServlet jerseyServlet = new SpringServlet();
		ServletHolder jerseyHolder = new ServletHolder(jerseyServlet);
		jerseyHolder.setInitParameter("com.sun.jersey.config.property.resourceConfigClass",
				"com.sun.jersey.api.core.PackagesResourceConfig");

		// auto-register all JAX-RS classes
		Set<String> packages = Sets.newHashSet();
		for (Class<?> app : apps) {
			packages.add(app.getPackage().getName());
		}
		jerseyHolder.setInitParameter("com.sun.jersey.config.property.packages", Joiner.on(",").join(packages));

		// figure out which URLs to serve via which servlet and in what order
		// first add Jersey servlets, then static
		if (rootRestUrls.contains("/")) {
			servletContextHandler.addServlet(jerseyHolder, "/*");
			// servletContextHandler.addFilter(OpenEntityManagerInViewFilter.class, "/*", FilterMapping.REQUEST);
		} else {
			for (String restUrl : rootRestUrls) {
				servletContextHandler.addServlet(jerseyHolder, restUrl + "/*");
				// servletContextHandler.addFilter(OpenEntityManagerInViewFilter.class, restUrl + "/*", FilterMapping.REQUEST);
			}
		}

		if (rootRestUrls.contains("/")) {
			LOG.info("JAX-RS service hooked up to /, static content will be server from /static");
			servletContextHandler.addServlet(staticHolder, "/static/*");

		} else {
			LOG.info("JAX-RS service NOT hooked up to /, static content will be server from / directly");
			servletContextHandler.addServlet(staticHolder, "/*");
		}

		setHandler(servletContextHandler);

		if (config.isInDevelopmentMode()) {
			LOG.info("Detected Napalm DEVELOPMENT mode");
		}
	}

	// finds out the root URLs all the REST classes are hooked up to from the @Path annotation
	private Set<String> getRootRestUrls(Class<?>... apps) {
		Set<String> urls = Sets.newHashSet();
		for (Class<?> app : apps) {
			if (app.isAnnotationPresent(Path.class)) {
				Path path = app.getAnnotation(Path.class);
				urls.add(path.value());
			}
		}
		return urls;
	}
}
