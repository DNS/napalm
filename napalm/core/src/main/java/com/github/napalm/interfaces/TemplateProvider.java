package com.github.napalm.interfaces;

import java.util.Map;

/**
 * Common interface for all template plugins
 * 
 * @author jacekf
 */
public interface TemplateProvider {
	public static final String TEMPLATE_ROOT = "templates/";

	/**
	 * Main rendering method
	 * 
	 * @return Rendered template from 'templates' resource folder
	 */
	public String render(String templateName, Map<String, ? extends Object> parameters);

	/**
	 * Main rendering method. Allows passing a set of key/valuess, e.g. "key1",value1,"key2",value2, etc. Workaround for the verbose
	 * Map creation syntax in Java
	 * 
	 * @return Rendered template from 'templates' resource folder
	 */
	public String render(String templateName, Object... keyValues);

	/**
	 * Main rendering method. Useful for those operations that are purely query-based, in which case you can just pass in a list of
	 * CallableOperation instances. The name of each operation automatically is the key in the Map of values passed to the
	 * templating provider
	 * 
	 * @return Rendered template from 'templates' resource folder
	 */
	public <DS extends Object, T extends Object> String render(String templateName, CallableOperation<DS, T>... queries);

}
