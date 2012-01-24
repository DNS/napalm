package com.github.napalm;

import com.github.napalm.spring.NapalmServer;

/**
 * Main entry point for Napalm apps
 */
public class Napalm {

	private static NapalmRunner defaultRunner = new NapalmRunner();
	
	/**
	 * Name of Spring entity containing the parsed Map<String,Object> from the YAML configuration file
	 * Can be @Autowired into plugins for parsing default configuration during start up
	 */
	public static final String CONFIG_MAP = "config";
	/**
	 * Name of int resource containing the PORT on which the app is running 
	 */
	public static final String PORT = "port";

	/**
	 * Adds a runtime resource (that can be accessed in Spring via @Resource). Should be executed BEFORE the start() or run()
	 * 
	 * @param key Resource key
	 * @param resource Object or String. If String, can be in "<jdbc url>,user,password" format, which will automatically create a
	 *        datasource for use in the app
	 */
	public static void addResource(String key, Object resource) {
		defaultRunner.addResource(key, resource);
	}

	/**
	 * Starts Napalm, but does not join to it
	 * 
	 * @param port Port
	 * @param apps List of root applications
	 */
	public static NapalmServer start(int port, Class<?>... apps) {
		return defaultRunner.start(port, apps);
	}

	/**
	 * Runs Napalm
	 * 
	 * @param port Port
	 * @param apps List of root applications
	 */
	public static void run(int port, Class<?>... apps) {
		defaultRunner.run(port, apps);
	}

	/**
	 * Stops Napalm
	 */
	public static void stop() {
		defaultRunner.stop();
	}
}
