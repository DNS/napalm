package com.github.napalm.spring;

import java.util.Map;
import java.util.Set;

import javax.tools.JavaCompiler;
import javax.tools.ToolProvider;
import javax.ws.rs.Path;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.context.WebApplicationContext;

import com.google.common.collect.Sets;

/**
 * Napalm configuration
 * 
 * @author jacekf
 * 
 */
@Service
public class NapalmConfig {

	public static final String PROP_NAPALM_DEV = "napalm.dev";

	@Autowired
	private WebApplicationContext ctx;

	/**
	 * Identifies if Napalm is running in DEV or PROD mode DEV is returned if running in JDK (vs JRE) or the 'napalm.dev' property
	 * is set to 'true'
	 */
	public boolean isInDevelopmentMode() {
		// check for napalm.dev property
		String dev = System.getProperty(PROP_NAPALM_DEV);
		if (dev != null) {
			return Boolean.parseBoolean(dev);
		}

		// check if JDK compiler present
		JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
		if (compiler != null) {
			return true;
		}

		return false;
	}

	/**
	 * @return The list of unique URLs that are being served via REST. if "/" is included, it is the only one that will be returned
	 */
	public Set<String> getRestUrls() {
		Map<String, Object> paths = ctx.getBeansWithAnnotation(Path.class);
		Set<String> urls = Sets.newLinkedHashSet();
		for (Object app : paths.values()) {
			String url = app.getClass().getAnnotation(Path.class).value();
			if ("/".equals(url)) {
				return Sets.newHashSet("/");
			} else {
				urls.add(url);
			}
		}
		return urls;
	}
}
