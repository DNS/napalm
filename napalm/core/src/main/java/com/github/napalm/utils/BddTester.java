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
 * A simple class to help with unit testing via BDD
 * using the best-of-breed Python and Ruby testing frameworks
 * @author jacekf
 */
public class BddTester {

	private static final Logger LOG = LoggerFactory.getLogger(BddTester.class);

	public static void testWithFreshen(String... tags) {
		testViaCommandline("./src/test/python", "nosetests","--with-freshen","-v");
	}

	public static void testWithLettuce(String... features) {
		testViaCommandline("./src/test/python", "lettuce");
	}

	public static void testWithCucumber() {
		testViaCommandline("./src/test/ruby", "cucumber");
	}

	/**
	 * Main test runner
	 * 
	 * @param workingDir
	 * @param commandLineArguments
	 */
	public static void testViaCommandline(String workingDir,
			String... commandLineArguments) {

		try {
			LOG.info("Running BDD tests from " + workingDir);

			StringBuilder bld = new StringBuilder();

			// merge the default and request-specific commands
			Set<String> commands = Sets.newLinkedHashSet(Arrays
					.asList(commandLineArguments));

			ProcessBuilder t = new ProcessBuilder(
					commands.toArray(new String[commands.size()]));

			File directory = new File(workingDir);

			t.directory(directory);
			t.redirectErrorStream(true);

			Process pr = t.start();
			int exitCode = pr.waitFor();
			BufferedReader buf = new BufferedReader(new InputStreamReader(
					pr.getInputStream()));
			String line = "";
			while ((line = buf.readLine()) != null) {
				bld.append(line).append("\n");
				LOG.info(line);
			}

			Assert.assertTrue(
					"BDD tests did not return any output. No unit test(s) found?",
					bld.length() > 0);
			Assert.assertEquals("BDD test(s) failed.", 0, exitCode);

		} catch (Exception ex) {
			throw new RuntimeException(ex);
		}

	}

}
