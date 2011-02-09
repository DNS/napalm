package com.github.napalm.freemarker;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.github.napalm.Napalm;
import com.github.napalm.utils.BddTester;

public class FreemarkerTest {

	@Before
	public void before() {
		Napalm.addResource("db", "jdbc:h2:mem:db");
		Napalm.start(8080, FreemarkerTestApp.class);
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
