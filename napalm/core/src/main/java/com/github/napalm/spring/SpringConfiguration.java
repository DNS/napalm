package com.github.napalm.spring;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportResource;
import org.springframework.context.annotation.Scope;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;

/**
 * A Java-Config for common dynamically-created Spring beans
 * 
 * @author jacekf
 */
@Configuration
@ImportResource("napalm/spring/napalmBeans.xml")
// needed for bootstrapping of @Transactional, @Async, @Scheduled support
public class SpringConfiguration {

	public static final String NAPALM_EXECUTOR = "napalmExecutor";
	public static final String NAPALM_SCHEDULER = "napalmScheduler";

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

}
