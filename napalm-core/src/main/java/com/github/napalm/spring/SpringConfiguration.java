package com.github.napalm.spring;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.eclipse.jetty.servlet.ServletContextHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * A Java-Config for common dynamically-created Spring beans
 * 
 * @author jacekf
 */
@Configuration
@EnableTransactionManagement
@EnableAsync
@EnableScheduling
public class SpringConfiguration {

	public static final String NAPALM_EXECUTOR = "napalmExecutor";
	public static final String NAPALM_SCHEDULER = "napalmScheduler";
	public static final String NAPALM_SERVLET_CONTEXT_HANDLER = "napalmServletContextHandler";

	/**
	 * @return A pre-configured executor with a fixed thread pool equal to the number of CPUs on the server
	 */
	@Bean(name = NAPALM_EXECUTOR)
	@Scope("singleton")
	public ExecutorService getNapalmExecutor() {
		return Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
	}

	/**
	 * @return A pre-configured task scheduler for @Async and @Scheduled tasks
	 */
	@Bean(name = NAPALM_SCHEDULER)
	@Scope("singleton")
	public TaskScheduler getNapalmScheduler() {
		return new ThreadPoolTaskScheduler();
	}

	/**
	 * @return The Jetty servlet context handler that plugins can jack into and customize during boot up
	 */
	@Bean(name = NAPALM_SERVLET_CONTEXT_HANDLER)
	@Scope("singleton")
	public ServletContextHandler getNapalmServletContextHander() {
		return new ServletContextHandler(ServletContextHandler.SESSIONS);
	}

}
