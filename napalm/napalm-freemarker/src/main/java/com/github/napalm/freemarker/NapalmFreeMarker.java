package com.github.napalm.freemarker;

import java.io.StringWriter;
import java.util.Map;
import java.util.concurrent.Callable;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import templates.TemplateDummyClass;

import com.github.napalm.interfaces.CallableOperation;
import com.github.napalm.interfaces.TemplateProvider;
import com.github.napalm.spring.NapalmConfig;
import com.github.napalm.spring.OperationExecutor;
import com.github.napalm.spring.TemplateHandler;
import com.google.common.collect.Maps;

import freemarker.template.Configuration;
import freemarker.template.DefaultObjectWrapper;
import freemarker.template.Template;

/**
 * Main Freemarker provider
 * 
 * @author jacekf
 */
@Service
public class NapalmFreeMarker implements TemplateProvider {

	@Autowired
	private TemplateHandler handler;
	@Autowired
	private OperationExecutor exec;
	@Autowired
	private NapalmConfig config;

	private Configuration cfg = new Configuration();

	@PostConstruct
	public void init() {
		cfg.setClassForTemplateLoading(TemplateDummyClass.class, "");
		cfg.setObjectWrapper(new DefaultObjectWrapper());

		// do not cache templates if running in dev mode
		if (config.isInDevelopmentMode()) {
			cfg.setTemplateUpdateDelay(0);
		}
	}

	/*
	 * (non-Javadoc)
	 * @see com.github.napalm.interfaces.TemplateProvider#render(java.lang.String, java.util.Map)
	 */
	@Override
	public String render(final String templateName, final Map<String, ? extends Object> parameters) {

		try {
			return handler.render(new Callable<String>() {
				@Override
				public String call() throws Exception {
					Template template = cfg.getTemplate(templateName);
					exec.execute(parameters);
					StringWriter sw = new StringWriter();
					template.process(parameters, sw);
					return sw.toString();
				}
			});
		} catch (RuntimeException e) {
			throw e;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
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

}
