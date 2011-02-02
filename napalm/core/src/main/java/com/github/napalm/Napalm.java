package com.github.napalm;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import lombok.Getter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;

import com.github.napalm.spring.NapalmServer;
import com.github.napalm.utils.DataSourceUtils;
import com.google.common.collect.Maps;

/**
 * Main entry point for Napalm apps
 */
public class Napalm {

	private static final Logger LOG = LoggerFactory.getLogger(Napalm.class);
	private static AnnotationConfigWebApplicationContext ctx = null;

	
	@Getter
	private static final Map<String, Object> resources = Maps.newHashMap();

	/**
	 * Adds a runtime resource (that can be accessed in Spring via @Resource). Should be executed BEFORE the start() or run()
	 * @param key Resource key
	 * @param resource Object or String. If String, can be in "<jdbc url>,user,password" format, which will automatically create a datasource for use in the app
	 */
	public static void addResource(String key, Object resource) {
		if (resource instanceof String) {
			//auto-create DataSource if required
			if (DataSourceUtils.isDataSource((String)resource)) {
				resources.put(key, DataSourceUtils.createDataSource((String)resource));
			} else {
				resources.put(key, resource);
			}
		} else {
			resources.put(key, resource);
		}
	}
	
	/**
	 * Starts Napalm, bu does not join to it
	 * 
	 * @param port Port
	 * @param apps List of root applications
	 */
	public static NapalmServer start(int port, Class<?>... apps) {
		try {

			if (ctx != null) {
				throw new RuntimeException("A Napalm server is already running!");
			}

			ctx = initSpring(apps);
			final NapalmServer web = ctx.getBean(NapalmServer.class);

			web.init(port, apps);
			web.start();

			LOG.info("Successfully started Napalm on port " + port);

			System.out.println("== Napalm has taken the stage...");
			System.out.println(">> Listening on 0.0.0.0:" + port);

			return web;

		} catch (RuntimeException ex) {
			throw ex;
		} catch (Exception ex) {
			throw new RuntimeException(ex);
		}
	}

	/**
	 * Runs Napalm
	 * 
	 * @param port Port
	 * @param apps List of root applications
	 */
	public static void run(int port, Class<?>... apps) {
		try {
			start(port, apps).join();
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
		NapalmServer web = ctx.getBean(NapalmServer.class);
		try {
			System.out.println("== Napalm is exiting the stage...");
			web.stop();
			System.out.println(">> Napalm has left the building.");
		} catch (RuntimeException ex) {
			throw ex;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	private static AnnotationConfigWebApplicationContext initSpring(Class<?>... apps) {
		AnnotationConfigWebApplicationContext ctx = new AnnotationConfigWebApplicationContext();
		ctx.refresh();
		
		// register Napalm and app-specific Spring beans
		Set<String> packages = new HashSet<String>();
		packages.add(NapalmServer.class.getPackage().getName());
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
