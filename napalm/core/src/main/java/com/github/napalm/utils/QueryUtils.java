package com.github.napalm.utils;

import java.io.IOException;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.lang.StringUtils;
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
	 * Query name can be either a straight name e.g. "allUsers" or with an alias "allUsers as users"
	 * 
	 * @param queryName Name or the specified alias
	 */
	public static String getQueryId(String queryName) {
		int pos = queryName.indexOf(" as ");
		if (pos > 0) {
			return queryName.substring(pos + 4);
		} else {
			return queryName;
		}
	}

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
						Map<String, Object> queries = (Map<String, Object>) obj;
						buildQueries(map, queries, null);
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
	 * Recursive method to parse all the YAML files
	 */
	@SuppressWarnings("unchecked")
	private static void buildQueries(Map<String, String> map, Map<String, Object> queries, String currentPrefix) {

		String previousQuery = null;

		for (Entry<String, Object> entry : queries.entrySet()) {
			if (entry.getValue() instanceof String) {
				// standalone query
				String name = getName(currentPrefix, entry.getKey());
				safePut(map, name, previousQuery, (String) entry.getValue());

				// only the first query in a map is used as the parent for all of them
				if (previousQuery == null) {
					previousQuery = (String) entry.getValue();
				}

			} else if (entry.getValue() instanceof Map) {
				// hierarchical query
				String prefix = (currentPrefix != null) ? currentPrefix + "." + entry.getKey() : entry.getKey();
				buildQueries(map, (Map<String, Object>) entry.getValue(), prefix);
			} else {
				throw new RuntimeException("Expected String or Map from YAML for key " + entry.getKey() + ", instead got: "
						+ entry.getValue());
			}
		}
	}

	private static String getName(String currentPrefix, String key) {
		if (StringUtils.isEmpty(currentPrefix)) {
			return key;
		} else {
			return String.format("%s.%s", currentPrefix, key);
		}
	}

	private static void safePut(Map<String, String> map, String key, String currentParentQuery, String query) {
		if (map.containsKey(key)) {
			throw new RuntimeException("Unable to add query key " + key + " as a query for this key already exists: "
					+ map.get(key));
		} else {
			if (StringUtils.isEmpty(currentParentQuery)) {
				map.put(key, query);
			} else {
				map.put(key, String.format("%s%s", currentParentQuery, query));
			}
		}
	}
}
