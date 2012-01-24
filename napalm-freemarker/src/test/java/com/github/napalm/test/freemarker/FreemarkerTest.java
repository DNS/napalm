package com.github.napalm.test.freemarker;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.github.napalm.Napalm;
import com.github.napalm.freemarker.NapalmFreeMarker;
import com.github.napalm.utils.BddTester;

public class FreemarkerTest {

	@Before
	public void before() {
		Napalm.start("src/test/resources/config/app.yml", FreemarkerTestApp.class, NapalmFreeMarker.class);
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
