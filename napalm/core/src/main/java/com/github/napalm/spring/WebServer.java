package com.github.napalm.spring;

import java.util.Set;

import org.eclipse.jetty.server.Connector;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.nio.SelectChannelConnector;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.context.WebApplicationContext;

import com.google.common.base.Joiner;
import com.google.common.collect.Sets;
import com.sun.jersey.spi.spring.container.servlet.SpringServlet;

@Service
public class WebServer extends Server {

	@Autowired private WebApplicationContext ctx;
	
	public void init(int port, Class<?>...apps) {
		
		Connector connector=new SelectChannelConnector();
        connector.setPort(port);
        setConnectors(new Connector[]{connector});
						
		ServletContextHandler context = new ServletContextHandler(ServletContextHandler.SESSIONS);
        context.setContextPath("/");
        context.setAttribute(WebApplicationContext.ROOT_WEB_APPLICATION_CONTEXT_ATTRIBUTE, ctx);
        
        SpringServlet servlet = new SpringServlet();
        ServletHolder holder = new ServletHolder(servlet);
        holder.setInitParameter("com.sun.jersey.config.property.resourceConfigClass","com.sun.jersey.api.core.PackagesResourceConfig");
        
        //auto-register all JAX-RS classes
        Set<String> packages = Sets.newHashSet();
    	for (Class<?> app : apps) {
    		packages.add(app.getPackage().getName());
    	}
        holder.setInitParameter("com.sun.jersey.config.property.packages",Joiner.on(",").join(packages));
        context.addServlet(holder, "/*");
        
        setHandler(context);
	}

}
