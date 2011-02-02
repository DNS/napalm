package com.github.napalm.spring;

import java.util.Map.Entry;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.stereotype.Service;

import com.github.napalm.Napalm;

/**
 * Adds the resources to the specified context
 * 
 * @author jacekf
 */
@Service
public class NapalmBeanFactoryPostProcessor implements BeanFactoryPostProcessor {

	private static final Logger LOG = LoggerFactory.getLogger(NapalmBeanFactoryPostProcessor.class);

	/*
	 * (non-Javadoc)
	 * @see
	 * org.springframework.beans.factory.config.BeanFactoryPostProcessor#postProcessBeanFactory(org.springframework.beans.factory
	 * .config.ConfigurableListableBeanFactory)
	 */
	@Override
	public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {
		for (Entry<String, Object> entry : Napalm.getResources().entrySet()) {
			LOG.debug("Adding {} as '{}' to Spring context", entry.getValue(), entry.getKey());
			beanFactory.registerSingleton(entry.getKey(), entry.getValue());
		}
	}
}