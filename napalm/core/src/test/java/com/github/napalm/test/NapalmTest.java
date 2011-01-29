package com.github.napalm.test;

import javax.ws.rs.GET;
import javax.ws.rs.Path;

import org.springframework.stereotype.Service;

import com.github.napalm.Napalm;

/**
 * Unit test for simple App.
 */
@Service
@Path("/")
public class NapalmTest {

	@GET
	public String get() {
		return "hi";
	}

	public static void main(String[] args) {
		Napalm.start(8080, NapalmTest.class);
		
		System.out.println("TEST");
		
		Napalm.stop();
	}

}
