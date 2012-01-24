package com.github.napalm;

import java.io.File;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;
import java.util.Set;

import lombok.Getter;
import lombok.SneakyThrows;

import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;
import org.yaml.snakeyaml.Yaml;

import com.github.napalm.spring.NapalmServer;
import com.github.napalm.utils.DataSourceUtils;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;

/**
 * The main runner for a Napalm app. Usually you would use the Napalm class directly (for a single app) but you could just
 * instantiate a separate NapalmRunner if you need to run multiple apps concurrently on different ports (e.g. mocks of external
 * systems during a unit test run).
 * 
 * @author jacekf
 * 
 */
public class NapalmRunner {

	private static final Logger LOG = LoggerFactory.getLogger(NapalmRunner.class);
	public static final String NAPALM_RUNNER = "napalmRunner";
	public static final String NAPALM_CLASSES = "napalmClasses";
	
	private AnnotationConfigWebApplicationContext ctx = null;
	
	@Getter
	private final Map<String, Object> resources = Maps.newHashMap();

	/**
	 * Adds a runtime resource (that can be accessed in Spring via @Resource). Should be executed BEFORE the start() or run()
	 * @param key Resource key
	 * @param resource Object or String. If String, can be in "<jdbc url>,user,password" format, which will automatically create a datasource for use in the app
	 */
	public void addResource(String key, Object resource) {
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
	public NapalmServer start(int port, Class<?>... apps) {
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
	public void run(int port, Class<?>... apps) {
		try {
			this.parseCommandLineOverrides();
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
	public void stop() {
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

	private AnnotationConfigWebApplicationContext initSpring(Class<?>... apps) {
		
		//parent ctx is needed to preserve singletons
		AnnotationConfigWebApplicationContext parentCtx = new AnnotationConfigWebApplicationContext();
		parentCtx.refresh();

		//add resources into scope
		for (Entry<String, Object> entry : getResources().entrySet()) {
			LOG.debug("Adding {} as '{}' to Spring context", entry.getValue(), entry.getKey());
			parentCtx.getBeanFactory().registerSingleton(entry.getKey(),entry.getValue());
		}
		
		//register singleton with Set of classes that make up this Napalm App
		Set<Class<?>> appSet = Sets.newLinkedHashSet(Arrays.asList(apps));
		parentCtx.getBeanFactory().registerSingleton(NAPALM_CLASSES, appSet);

		//main context - change to XML
		AnnotationConfigWebApplicationContext ctx = new AnnotationConfigWebApplicationContext();
		ctx.setParent(parentCtx);
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
	
	/**
	 * Parses a default YAML file with configuration information
	 * @param configPath
	 */
	@SuppressWarnings("unchecked")
	@SneakyThrows
	private void parseConfig(String configPath) {
		Yaml parser = new Yaml();
		String yaml = FileUtils.readFileToString(new File(configPath));
		Map<String,Object> config = (Map<String, Object>) parser.load(yaml);
		
		//store the entire config as a resource
		addResource(Napalm.CONFIG_MAP, config);
		
		//parse port
		if(config.containsKey(Napalm.PORT)) {
			addResource(Napalm.PORT, config.get(Napalm.PORT));
		}
		
		//add default JDBC resources
		if (config.containsKey("jdbc")) {
			Map<String,String> jdbc = (Map<String, String>) config.get("jdbc");
			for(Entry<String,String> entry : jdbc.entrySet()) {
				addResource(entry.getKey(), entry.getValue());
			}
		}
	}
	
	/**
	 * Allows to overriden any of the settings from the base YAML file via a command line arguments
	 */
	private void parseCommandLineOverrides() {
		Properties props = System.getProperties();
		//take only the properties we are interested in
		for(Entry<Object,Object> entry : props.entrySet()) {
			String key = String.valueOf(entry.getKey());
			
			if (Napalm.PORT.equals(entry.getKey())) {
				addResource(Napalm.PORT, Integer.parseInt(String.valueOf(entry.getValue())));
			} else if (key.startsWith("jdbc.")) {
				//add JDBC resources
				String resourceName = key.substring(5);
				addResource(resourceName, String.valueOf(entry.getValue()));
			}
		}
	}

	
}
