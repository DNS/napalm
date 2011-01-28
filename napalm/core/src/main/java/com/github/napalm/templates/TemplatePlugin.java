package com.github.napalm.templates;

import java.util.Map;

/**
 * Common interface for all template plugins
 * 
 * @author jacekf
 */
public interface TemplatePlugin {
	public static final String TEMPLATE_ROOT = "templates";

	/**
	 * Main rendering method
	 * 
	 * @return Rendered template from 'templates' resource folder
	 */
	public String render(String templateName, Map<String, ? extends Object> parameters);

}
