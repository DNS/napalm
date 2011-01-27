package org.napalm4j;

import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;

/**
 * Main entry point for Napalm apps
 */
public class Napalm {

    public static <T> void run(int port, Class<T> app) {
        try {

        	//WebServerService server = new WebServerService();
        	//server.start(port, app);
        		
        	AnnotationConfigWebApplicationContext ctx = initSpring(app);
        	//WebServerService web = ctx.getBean(WebServerService.class);
        	//WebServerService web = new WebServerService();
        	//web.start(ctx, port, app);
        	
        	System.out.println("== Napalm has taken the stage...");
        	System.out.println(">> Listening on 0.0.0.0:" + port);
        	
        	//web.join();
        	
        } catch (RuntimeException ex) {
            throw ex;
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    public static void stop() {
        //TODO
    }
    
    private static <T> AnnotationConfigWebApplicationContext initSpring(Class<T> app) {
    	AnnotationConfigWebApplicationContext ctx = new AnnotationConfigWebApplicationContext();
    	//register Napalm and app-specific Spring beans
    	ctx.setConfigLocations(new String[]{app.getPackage().getName()});
    	ctx.refresh();
    	
    	//auto-register all JAX-RS beans with CXF
    	//SpringJAXRSServerFactoryBean jaxrs = ctx.getBean(SpringJAXRSServerFactoryBean.class);
    	//Map<String,Object> beans = ctx.getBeansWithAnnotation(Path.class);
    	//jaxrs.setServiceBeans(new ArrayList<Object>(beans.values()));
    	//jaxrs.create();

    	ctx.refresh();
    	ctx.registerShutdownHook();
    	ctx.start();
    	
    	return ctx;
    }
}
