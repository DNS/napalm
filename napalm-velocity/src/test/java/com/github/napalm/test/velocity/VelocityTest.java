package com.github.napalm.test.velocity;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.github.napalm.Napalm;
import com.github.napalm.utils.BddTester;
import com.github.napalm.velocity.NapalmVelocity;

public class VelocityTest {

	@Before
	public void before() {
		Napalm.start(null, VelocityTestApp.class, NapalmVelocity.class);
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
