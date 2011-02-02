package com.github.napalm.test;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.github.napalm.Napalm;
import com.github.napalm.test.app.NapalmTestApp;
import com.github.napalm.utils.BddTester;

public class CoreTest {

	@Before
	public void before() {
		Napalm.addResource("db", "jdbc:h2:mem:db1");
		Napalm.start(8080, NapalmTestApp.class);
	}

	@After
	public void after() {
		Napalm.stop();
	}

	@Test
	public void bddTests() {
		BddTester.testWithFreshen();
	}
}
