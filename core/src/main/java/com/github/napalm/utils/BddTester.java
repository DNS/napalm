package com.github.napalm.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.Set;

import org.junit.Assert;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.Sets;

/**
 * A simple class to help with unit testing via BDD using the best-of-breed Python and Ruby testing frameworks. Say goodbye to the
 * archaic verbosity of JUnit.
 * 
 * Usage: Write a base JUnit class with a single test method. In the @Before Naplam.start() your app, in the @After Napalm.stop()
 * 
 * In the unit test itself, just call BddTester.testWithFreshen() or whichever other BDD framework you prefer
 * 
 * @author jacekf
 */
public class BddTester {

	private static final Logger LOG = LoggerFactory.getLogger(BddTester.class);

	/**
	 * Runs BDD tests in src/test/python/features using the Freshen (the Python BDD framework) To install run
	 * "sudo easy_install -U freshen" on any box with python-setuptools installed
	 */
	public static void testWithFreshen() {
		testViaCommandline("./src/test/python", "nosetests", "--with-freshen", "-v");
	}

	/**
	 * Runs BDD tests in src/test/python/features using the Freshen (the Python BDD framework) To install run
	 * "sudo easy_install -U lettuce" on any box with python-setuptools installed
	 */
	public static void testWithLettuce() {
		testViaCommandline("./src/test/python", "lettuce");
	}

	/**
	 * Runs BDD tests in src/test/python/features using the Cucumber (the Ruby BDD framework) To install run
	 * "sudo gem install cucumber" on any box with a working Ruby gems installation\
	 */
	public static void testWithCucumber() {
		testViaCommandline("./src/test/ruby", "cucumber");
	}

	/**
	 * Main test runner
	 * 
	 * @param workingDir
	 * @param commandLineArguments
	 */
	public static void testViaCommandline(String workingDir, String... commandLineArguments) {

		try {
			LOG.info("Running BDD tests from " + workingDir);

			StringBuilder bld = new StringBuilder();

			// merge the default and request-specific commands
			Set<String> commands = Sets.newLinkedHashSet(Arrays.asList(commandLineArguments));

			ProcessBuilder t = new ProcessBuilder(commands.toArray(new String[commands.size()]));

			File directory = new File(workingDir);

			t.directory(directory);
			t.redirectErrorStream(true);

			Process pr = t.start();
			int exitCode = pr.waitFor();
			BufferedReader buf = new BufferedReader(new InputStreamReader(pr.getInputStream()));
			String line = "";
			while ((line = buf.readLine()) != null) {
				bld.append(line).append("\n");
				LOG.info(line);
			}

			String log = bld.toString();

			Assert.assertTrue("BDD tests did not return any output. No unit test(s) found?\n" + log, bld.length() > 0);
			Assert.assertEquals("BDD test(s) failed.\n" + log, 0, exitCode);
			Assert.assertTrue("BDD test(s) failed due to UNDEFINED steps.\n" + log, !bld.toString().contains("UNDEFINED"));

			// all OK, print out log
			System.out.println(log);

		} catch (Exception ex) {
			throw new RuntimeException(ex);
		}

	}
}
