package com.github.napalm.jpa.config;

import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import javax.annotation.PostConstruct;
import javax.persistence.EntityManagerFactory;

import org.eclipse.jetty.servlet.FilterHolder;
import org.eclipse.jetty.servlet.FilterMapping;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.jpa.support.OpenEntityManagerInViewFilter;
import org.springframework.stereotype.Service;
import org.springframework.web.context.WebApplicationContext;

import com.github.napalm.spring.NapalmConfig;

/**
 * Some extra util init code that could not have been put into JpaBeanFactoryPostProcessor
 * 
 * @author jacekf
 */
@Service
public class JpaInitializer {

	@Autowired
	private ServletContextHandler servletContextHandler;
	@Autowired
	private NapalmConfig config;
	@Autowired
	private WebApplicationContext ctx;

	@PostConstruct
	public void init() {
		// auto-register the OpenEntityManagerInViewFilter filter
		Map<String, EntityManagerFactory> emfs = ctx.getBeansOfType(EntityManagerFactory.class);

		for (Entry<String, EntityManagerFactory> emf : emfs.entrySet()) {

			Set<String> restUrls = config.getRestUrls();
			FilterHolder filterHolder = new FilterHolder(OpenEntityManagerInViewFilter.class);
			filterHolder.setInitParameter("entityManagerFactoryBeanName", emf.getKey()); // bind it to a particular EMF
			for (String restUrl : restUrls) {
				if (restUrl.endsWith("/")) {
					servletContextHandler.addFilter(filterHolder, restUrl + "*", FilterMapping.REQUEST);
				} else {
					servletContextHandler.addFilter(filterHolder, restUrl + "/*", FilterMapping.REQUEST);
				}
			}

		}

	}
}
