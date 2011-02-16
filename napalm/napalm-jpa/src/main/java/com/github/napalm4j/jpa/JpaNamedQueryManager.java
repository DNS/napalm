package com.github.napalm4j.jpa;

import java.util.concurrent.ConcurrentHashMap;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.stereotype.Service;

import com.github.napalm.spring.NapalmConfig;
import com.github.napalm.utils.QueryUtils;

/**
 * Manages all the named JPA queries
 * 
 * @author jacekf
 * 
 */
@Service
public class JpaNamedQueryManager {

	@Autowired
	private NapalmConfig config;
	@Autowired
	private TaskScheduler scheduler;

	private ConcurrentHashMap<String, String> queries = new ConcurrentHashMap<String, String>();

	@PostConstruct
	public void init() {
		// find all the user defined queries
		queries.putAll(QueryUtils.parseQueries("jpa"));

		// if in dev mode, reload every second in the background
		if (config.isInDevelopmentMode()) {
			scheduler.scheduleAtFixedRate(new Runnable() {
				@Override
				public void run() {
					queries.putAll(QueryUtils.parseQueries("jpa"));
				}
			}, 1000);
		}
	}

	/**
	 * Gets named query syntax
	 * 
	 * @param name Name of query defined in a classpath:jpa/*.yml file
	 * @return Query syntax or null if not found
	 */
	public String getQuery(String name) {
		return queries.get(name);
	}

}
