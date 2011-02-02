package com.github.napalm.utils;

import javax.sql.DataSource;

import org.apache.commons.dbcp.BasicDataSource;
import org.apache.commons.lang.StringUtils;
import org.javatuples.Triplet;

/**
 * Utils for creating datasources before startup
 * 
 * @author jacekf
 * 
 */
public class DataSourceUtils {

	/**
	 * Determines if String resource is a JDBC connection URL or not
	 * 
	 * @param resource
	 * @return
	 */
	public static boolean isDataSource(String resource) {
		if (resource != null && resource.startsWith("jdbc:")) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * Parses the resource to extract the JDBC URL, the user, the password
	 * 
	 * @param resource
	 * @return Triplet
	 */
	public static Triplet<String, String, String> parseResource(String resource) {
		String url = null, user = null, password = null;

		String[] parts = StringUtils.split(resource, ",");
		url = parts[0];
		if (parts.length > 1) {
			user = parts[1];
		}
		if (parts.length > 2) {
			password = parts[2];
		}
		return new Triplet<String, String, String>(url, user, password);
	}

	/**
	 * Creates a DataSource from a String in the format '<jdbc url>,user,password', e.g.
	 * "jdbc:mysql://localhost:3306/mydb,myuser,mypassword"
	 * 
	 * @param resource
	 * @return
	 */
	public static DataSource createDataSource(String resource) {
		Triplet<String, String, String> t = parseResource(resource);

		BasicDataSource ds = new BasicDataSource();
		ds.setUrl(t.getValue0());
		if (t.getValue1() != null) {
			ds.setUsername(t.getValue1());
		}
		if (t.getValue2() != null) {
			ds.setPassword(t.getValue2());
		}

		// set up refresh query
		if (t.getValue0().toLowerCase().contains("oracle")) {
			ds.setValidationQuery("SELECT 1 FROM DUAL");
		} else {
			ds.setValidationQuery("SELECT 1");
		}

		return ds;
	}
}
