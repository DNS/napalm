package com.github.napalm4j.sql;

import static org.junit.Assert.*;

import java.util.Map;

import org.junit.Test;

import com.github.napalm.utils.QueryUtils;

/**
 * Unit test for simple App.
 */
public class NapalmSqlTest {

	@Test
	public void testUtilsParsing() {
		Map<String, String> map = QueryUtils.parseQueries("sql");

		assertEquals(4, map.size());
		assertEquals("SELECT *\nFROM USERS", map.get("allUsers").trim());
		assertEquals("SELECT * FROM USERS", map.get("users").trim());
		assertEquals("SELECT * FROM USERS WHERE USERNAME = ?", map.get("users.byUsername").trim());
		assertEquals("SELECT * FROM USERS WHERE BIRTH_DATE = ?", map.get("users.byBirthDate").trim());
	}
}
