package org.napalm4j.spring;

import javax.ws.rs.POST;

import org.eclipse.jetty.server.Server;
import org.springframework.stereotype.Service;

@Service
public class WebServer extends Server {

	
	public void init() {
		ServletContextHandler context = new ServletContextHandler(ServletContextHandler.SESSIONS);
        context.setContextPath("/");
        setHandler(context);
 		
	}
	
	
}
