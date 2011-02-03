package com.github.napalm4j.sql;

import static org.junit.Assert.*;

import java.util.Map;

import org.junit.Test;

import com.github.napalm4j.sql.query.QueryUtils;

/**
 * Unit test for simple App.
 */
public class NapalmSqlTest {

	@Test
	public void testUtilsParsing() {
		Map<String, String> map = QueryUtils.parseQueries();

		assertEquals(4, map.size());
		assertEquals("SELECT * FROM\nUSERS", map.get("allUsers").trim());
		assertEquals("SELECT * FROM USERS", map.get("users.base").trim());
		assertEquals("SELECT * FROM USERS\nWHERE USERNAME = ?", map.get("users.byUsername").trim());
		assertEquals("SELECT * FROM USERS\nWHERE BIRTH_DATE = ?", map.get("users.byBirthDate").trim());
	}
}
