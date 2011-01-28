package com.github.napalm;

import java.util.HashSet;
import java.util.Set;

import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;

import com.github.napalm.spring.WebServer;

/**
 * Main entry point for Napalm apps
 */
public class Napalm {

	private static AnnotationConfigWebApplicationContext ctx = null;
	
    /**
     * Runs Napalm
     * @param port Port
     * @param apps List of root applications
     */
    public static void run(int port, Class<?>... apps) {
        try {
       		
        	if (ctx != null) {
        		throw new RuntimeException("A Napalm server is already running!");
        	}
        	
        	ctx = initSpring(apps);
        	WebServer web = ctx.getBean(WebServer.class);
        	web.init(port, apps);
        	
        	web.start();
        	
        	System.out.println("== Napalm has taken the stage...");
        	System.out.println(">> Listening on 0.0.0.0:" + port);
        	
          	web.join();
        	
        } catch (RuntimeException ex) {
            throw ex;
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    /**
     * Stops Napalm
     */
    public static void stop() {
        WebServer web = ctx.getBean(WebServer.class);
        try {
			web.stop();
        } catch (RuntimeException ex) {
            throw ex;			
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
    }
    
    private static AnnotationConfigWebApplicationContext initSpring(Class<?>... apps) {
    	AnnotationConfigWebApplicationContext ctx = new AnnotationConfigWebApplicationContext();
    	//register Napalm and app-specific Spring beans
    	Set<String> packages = new HashSet<String>();
    	packages.add(WebServer.class.getPackage().getName());
    	for (Class<?> app : apps) {
    		packages.add(app.getPackage().getName());
    	}
    	
    	ctx.setConfigLocations(packages.toArray(new String[packages.size()]));

    	ctx.refresh();
    	ctx.registerShutdownHook();
    	ctx.start();
    	
    	return ctx;
    }
}
