package com.github.napalm.utils;

import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.yaml.snakeyaml.Yaml;

import com.google.common.collect.Maps;

/**
 * Utils class responsible for parsing the external SQL YAML files
 * 
 * @author jacekf
 * 
 */
public class QueryUtils {

	/**
	 * Parses all the classpath:sql/*.yml files it can find in the classpath and build a map of pre-defined queries
	 */
	@SuppressWarnings("unchecked")
	public static Map<String, String> parseQueries(String classpathRoot) {
		Map<String, String> map = Maps.newLinkedHashMap();

		try {
			Resource[] resources = new PathMatchingResourcePatternResolver(ClassLoader.getSystemClassLoader())
					.getResources("classpath*:" + classpathRoot + "/**/*.yml");

			for (Resource resource : resources) {
				Yaml parser = new Yaml();
				Iterator<Object> it = parser.loadAll(resource.getInputStream()).iterator();
				while (it.hasNext()) {
					Object obj = it.next();
					if (obj instanceof Map) {
						buildQueries(map, (Map<String, Object>) obj);
					} else {
						throw new RuntimeException("Expected Map from YAML, instead got: " + obj.getClass());
					}

				}
			}

		} catch (IOException e) {
			throw new RuntimeException(e);
		}

		return map;
	}

	/**
	 * Builds a list of queries from a single Yaml resource
	 */
	@SuppressWarnings("unchecked")
	private static void buildQueries(Map<String, String> map, Map<String, Object> queries) {

		for (Entry<String, Object> entry : queries.entrySet()) {
			if (entry.getValue() instanceof String) {
				// standalone query
				safePut(map, entry.getKey(), (String) entry.getValue());

			} else if (entry.getValue() instanceof List) {
				// hierarchical query
				List<Object> parts = (List<Object>) entry.getValue();

				String rootQuery = null;

				for (int i = 0; i < parts.size(); i++) {
					if (i == 0) {
						if (parts.get(0) instanceof String) {
							rootQuery = (String) parts.get(0);
							safePut(map, entry.getKey(), rootQuery);
						} else {
							throw new RuntimeException("In a hierarchical query, the first element must be a String, instead got "
									+ parts.get(0));
						}
					} else {
						if (parts.get(i) instanceof Map) {
							Map<String, Object> childQuery = (Map<String, Object>) parts.get(i);
							for (Entry<String, Object> childEntry : childQuery.entrySet()) {
								safePut(map, entry.getKey() + "." + childEntry.getKey(), rootQuery + " " + childEntry.getValue());
							}
						} else {
							throw new RuntimeException(
									"In a hierarchical query, all the 2nd and higher elements must be a Map, instead got "
											+ parts.get(i));
						}
					}
				}

				// hierarchical query
				// String prefix = (currentPrefix != null) ? currentPrefix + "." + entry.getKey() : entry.getKey();
				// buildQueries(map, (Map<String, Object>) entry.getValue(), prefix);
			} else {
				throw new RuntimeException("Expected String or List from YAML for key " + entry.getKey() + ", instead got: "
						+ entry.getValue());
			}
		}
	}

	private static void safePut(Map<String, String> map, String key, String query) {
		if (map.containsKey(key)) {
			throw new RuntimeException("Unable to add query key " + key + " as a query for this key already exists: "
					+ map.get(key));
		} else {
			map.put(key, query);
		}
	}

}
