package com.github.napalm.test;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.github.napalm.NapalmRunner;
import com.github.napalm.test.app.NapalmRestTestApp;
import com.github.napalm.test.app.NapalmStaticTestApp;
import com.github.napalm.utils.BddTester;

/**
 * Starts up 2 separate napalm apps on two different ports (each with different static/rest setups
 * 
 * @author jacekf
 * 
 */
public class CoreTest {

	private NapalmRunner restRunner = new NapalmRunner();
	private NapalmRunner staticRunner = new NapalmRunner();

	@Before
	public void before() {
		restRunner.addResource("db", "jdbc:h2:mem:db1");
		restRunner.start(8080, NapalmRestTestApp.class);

		staticRunner.addResource("db", "jdbc:h2:mem:db2");
		staticRunner.start(8081, NapalmStaticTestApp.class);
	}

	@After
	public void after() {
		restRunner.stop();
		staticRunner.stop();
	}

	@Test
	public void bddTests() {
		BddTester.testWithFreshen();
	}
}
