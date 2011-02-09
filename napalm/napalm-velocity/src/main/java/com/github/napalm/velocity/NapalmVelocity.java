package com.github.napalm.velocity;

import java.io.StringWriter;
import java.util.Map;
import java.util.concurrent.Callable;

import javax.annotation.PostConstruct;

import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;
import org.apache.velocity.runtime.RuntimeServices;
import org.apache.velocity.runtime.log.LogChute;
import org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.napalm.interfaces.CallableOperation;
import com.github.napalm.interfaces.TemplateProvider;
import com.github.napalm.spring.NapalmConfig;
import com.github.napalm.spring.OperationExecutor;
import com.github.napalm.spring.TemplateHandler;
import com.google.common.collect.Maps;

/**
 * Napalm Velocity engine, pre-configured for fetching templates from classpath "templates" folder
 */
@Service
public class NapalmVelocity extends VelocityEngine implements LogChute, TemplateProvider {

	private static final Logger LOG = LoggerFactory.getLogger(NapalmVelocity.class);

	@Autowired
	private TemplateHandler handler;
	@Autowired
	private OperationExecutor exec;
	@Autowired
	private NapalmConfig config;

	@PostConstruct
	public void postConstruct() {
		setProperty(VelocityEngine.RUNTIME_LOG_LOGSYSTEM, this);
		setProperty(VelocityEngine.RESOURCE_LOADER, "class");
		setProperty("class.resource.loader.class", ClasspathResourceLoader.class.getName());
		setProperty("class.resource.loader.path", "templates");

		// force reloading of templates if running in dev mode
		if (config.isInDevelopmentMode()) {
			setProperty("class.resource.loader.modificationCheckInterval", "1");
		}

		setProperty("eventhandler.include.class", "org.apache.velocity.app.event.implement.IncludeRelativePath");
		init();
	}

	/*
	 * (non-Javadoc)
	 * @see com.github.napalm.interfaces.TemplateProvider#render(java.lang.String, java.util.Map)
	 */
	@Override
	public String render(final String templateName, final Map<String, ? extends Object> parameters) {
		return handler.render(new Callable<String>() {
			@Override
			public String call() throws Exception {
				Template template = getTemplate(TEMPLATE_ROOT + templateName);
				VelocityContext context = new VelocityContext(parameters);

				exec.execute(parameters); // fan out to executor to handle any CallableOperations that were passed in

				StringWriter writer = new StringWriter();
				template.merge(context, writer);
				return writer.toString();
			}
		});
	}

	/*
	 * (non-Javadoc)
	 * @see com.github.napalm.interfaces.TemplateProvider#render(java.lang.String, java.lang.Object[])
	 */
	@Override
	public String render(String templateName, Object... keyValuePairs) {
		Map<String, Object> parameters = Maps.newHashMap();
		for (int i = 0; i < keyValuePairs.length; i = i + 2) {
			parameters.put(String.valueOf(keyValuePairs[i]), keyValuePairs[i + 1]);
		}
		return render(templateName, parameters);
	}

	/*
	 * (non-Javadoc)
	 * @see com.github.napalm.interfaces.TemplateProvider#render(java.lang.String,
	 * com.github.napalm.interfaces.CallableOperation<DS,T>[])
	 */
	@Override
	public <DS, T> String render(String templateName, CallableOperation<DS, T>... queries) {
		Map<String, Object> parameters = Maps.newHashMap();
		for (CallableOperation<DS, T> query : queries) {
			parameters.put(query.getName(), query);
		}
		return render(templateName, parameters);
	}

	/*
	 * (non-Javadoc)
	 * @see org.apache.velocity.runtime.log.LogChute#init(org.apache.velocity.runtime .RuntimeServices)
	 */
	@Override
	public void init(RuntimeServices rs) throws Exception {
		LOG.info("Initializing Napalm Velocity Engine...");
	}

	/*
	 * (non-Javadoc)
	 * @see org.apache.velocity.runtime.log.LogChute#log(int, java.lang.String)
	 */
	@Override
	public void log(int level, String message) {
		switch (level) {
		case LogChute.DEBUG_ID:
			LOG.debug(message);
			break;
		case LogChute.ERROR_ID:
			LOG.error(message);
			break;
		case LogChute.INFO_ID:
			LOG.info(message);
			break;
		case LogChute.TRACE_ID:
			LOG.trace(message);
			break;
		case LogChute.WARN_ID:
			LOG.warn(message);
		default:
			break;
		}
	}

	/*
	 * (non-Javadoc)
	 * @see org.apache.velocity.runtime.log.LogChute#log(int, java.lang.String, java.lang.Throwable)
	 */
	@Override
	public void log(int level, String message, Throwable t) {
		switch (level) {
		case LogChute.DEBUG_ID:
			LOG.debug(message, t);
			break;
		case LogChute.ERROR_ID:
			LOG.error(message, t);
			break;
		case LogChute.INFO_ID:
			LOG.info(message, t);
			break;
		case LogChute.TRACE_ID:
			LOG.trace(message, t);
			break;
		case LogChute.WARN_ID:
			LOG.warn(message, t);
		default:
			break;
		}
	}

	/*
	 * (non-Javadoc)
	 * @see org.apache.velocity.runtime.log.LogChute#isLevelEnabled(int)
	 */
	@Override
	public boolean isLevelEnabled(int level) {
		switch (level) {
		case LogChute.DEBUG_ID:
			return LOG.isDebugEnabled();
		case LogChute.ERROR_ID:
			return LOG.isErrorEnabled();
		case LogChute.INFO_ID:
			return LOG.isInfoEnabled();
		case LogChute.TRACE_ID:
			return LOG.isTraceEnabled();
		case LogChute.WARN_ID:
			return LOG.isWarnEnabled();
		default:
			return false;
		}
	}

}
