package com.github.napalm.spring;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

/**
 * A Java-Config for common dynamically-created Spring beans
 * 
 * @author jacekf
 */
@Configuration
public class SpringConfiguration {

	public static final String NAPALM_EXECUTOR = "napalmExecutor";

	/**
	 * @return A pre-configured executor with a fixed thread pool equal to the number of CPUs on the server
	 */
	@Bean(name = NAPALM_EXECUTOR)
	@Scope("singleton")
	public ExecutorService getNapalmExecutor() {
		return Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
	}

}
