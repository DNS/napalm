package com.github.napalm.test;

import static com.github.napalm.utils.DataSourceUtils.*;
import static org.junit.Assert.*;

import org.apache.commons.dbcp.BasicDataSource;
import org.javatuples.Triplet;
import org.junit.Test;

/**
 * The only JUnit test we have for low-level testing of Java code outside of the Freshen integration testing
 * 
 * @author jacekf
 * 
 */
public class NapalmUtilTest {

	@Test
	public void testFullJdbcResource() {
		assertEquals(true, isDataSource("jdbc:mysql://localhost:3306/napalm"));
	}

	@Test
	public void testJdbcResourceParsing() {
		Triplet<String, String, String> t = new Triplet<String, String, String>("jdbc:mysql://localhost:3306/napalm", null, null);
		assertEquals(t, parseResource("jdbc:mysql://localhost:3306/napalm"));

		t = new Triplet<String, String, String>("jdbc:mysql://localhost:3306/napalm", "user", null);
		assertEquals(t, parseResource("jdbc:mysql://localhost:3306/napalm,user"));

		t = new Triplet<String, String, String>("jdbc:mysql://localhost:3306/napalm", "user", "password");
		assertEquals(t, parseResource("jdbc:mysql://localhost:3306/napalm,user,password"));
	}

	@Test
	public void testDataSourceGeneration() {
		BasicDataSource ds = (BasicDataSource) createDataSource("jdbc:mysql://localhost:3306/napalm");
		assertEquals("jdbc:mysql://localhost:3306/napalm", ds.getUrl());
		assertEquals(null, ds.getUsername());
		assertEquals(null, ds.getPassword());

		ds = (BasicDataSource) createDataSource("jdbc:mysql://localhost:3306/napalm,user");
		assertEquals("jdbc:mysql://localhost:3306/napalm", ds.getUrl());
		assertEquals("user", ds.getUsername());
		assertEquals(null, ds.getPassword());

		ds = (BasicDataSource) createDataSource("jdbc:mysql://localhost:3306/napalm,user,password");
		assertEquals("jdbc:mysql://localhost:3306/napalm", ds.getUrl());
		assertEquals("user", ds.getUsername());
		assertEquals("password", ds.getPassword());
	}
}
