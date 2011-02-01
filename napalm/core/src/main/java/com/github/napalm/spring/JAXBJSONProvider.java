package com.github.napalm.spring;

import java.util.concurrent.ConcurrentHashMap;

import javax.ws.rs.ext.Provider;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

/**
 * Allows returning JSON payloads from Napalm
 * 
 * @author jacekf
 * 
 */
@Provider
@Service
public class JAXBJSONProvider {

	private static final Logger LOG = LoggerFactory.getLogger(JAXBJSONProvider.class);

	// cache the contexts for each class, to avoid the need to re-create them
	private ConcurrentHashMap<Class<?>, JAXBContext> contexts = new ConcurrentHashMap<Class<?>, JAXBContext>();

	public JAXBContext getContext(Class<?> aClass) {
		try {
			JAXBContext ctx = contexts.get(aClass);
			if (ctx == null) {
				synchronized (contexts) {
					LOG.debug("Creating new JAXBContext for {}", aClass.getSimpleName());
					ctx = JAXBContext.newInstance(aClass);
					contexts.put(aClass, ctx);
				}
			}
			return ctx;

		} catch (JAXBException e) {
			throw new RuntimeException(e);
		}
	}
}
