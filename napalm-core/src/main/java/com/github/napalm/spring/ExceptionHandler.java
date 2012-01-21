package com.github.napalm.spring;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;

import org.springframework.stereotype.Service;

@Service
public class ExceptionHandler implements ExceptionMapper<Throwable> {

	@Override
	public Response toResponse(Throwable arg0) {
		System.out.println("TEST");
		// TODO Auto-generated method stub
		return null;
	}

}
