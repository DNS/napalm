package org.napalm4j;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.ContextHandler;
import org.eclipse.jetty.server.handler.ContextHandler.Context;
import org.eclipse.jetty.servlet.ServletContextHandler;

/**
 * Hello world!
 *
 */
public class Napalm {

    public void run(NapalmApplication app, int port) {
        try {

            Server server = new Server(port);

            ServletContextHandler context = new ServletContextHandler();
            context.setContextPath("/"); //get from app @Path
            context.setResourceBase("."); //,a
            context.setClassLoader(Thread.currentThread().getContextClassLoader());
            
            server.setHandler(context);

            

            //root.addEventListener(new SampleConfig());
            //root.addFilter(GuiceFilter.class, "/*", 0);

            //root.addServlet(EmptyServlet.class, "/*");

            server.start();



        } catch (RuntimeException ex) {
            throw ex;
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    public void stop(NapalmApplication app) {
        //TODO
    }
}
