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

import com.github.napalm.spring.TemplateHandler;
import com.github.napalm.templates.TemplatePlugin;

/**
 * Napalm Velocity engine, pre-configured for fetching templates from classpath
 * "templates" folder
 */
@Service
public class NapalmVelocityEngine extends VelocityEngine implements LogChute,
		TemplatePlugin {

	private static final Logger LOG = LoggerFactory
			.getLogger(NapalmVelocityEngine.class);

	@Autowired
	private TemplateHandler handler;

	@PostConstruct
	public void postConstruct() {
		setProperty(VelocityEngine.RUNTIME_LOG_LOGSYSTEM, this);
		setProperty(VelocityEngine.RESOURCE_LOADER, "class");
		setProperty("class.resource.loader.class",
				ClasspathResourceLoader.class.getName());
		setProperty("class.resource.loader.path", "templates");
		setProperty("class.resource.loader.modificationCheckInterval", "1");
		setProperty("eventhandler.include.class",
				"org.apache.velocity.app.event.implement.IncludeRelativePath");
		init();
	}

	/**
	 * Main rendering function
	 * 
	 * @param templateName
	 * @param parameters
	 * @return
	 */
	public String render(final String templateName,
			final Map<String, ? extends Object> parameters) {
		return handler.render(new Callable<String>() {
			@Override
			public String call() throws Exception {
				Template template = getTemplate(TEMPLATE_ROOT + "/"
						+ templateName);
				VelocityContext context = new VelocityContext(parameters);
				StringWriter writer = new StringWriter();
				template.merge(context, writer);
				return writer.toString();
			}
		});
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.apache.velocity.runtime.log.LogChute#init(org.apache.velocity.runtime
	 * .RuntimeServices)
	 */
	@Override
	public void init(RuntimeServices rs) throws Exception {
		// nothing to do
	}

	/*
	 * (non-Javadoc)
	 * 
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
	 * 
	 * @see org.apache.velocity.runtime.log.LogChute#log(int, java.lang.String,
	 * java.lang.Throwable)
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
	 * 
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
