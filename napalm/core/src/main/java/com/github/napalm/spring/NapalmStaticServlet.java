package com.github.napalm.spring;

import org.eclipse.jetty.servlet.DefaultServlet;
import org.eclipse.jetty.util.resource.Resource;
import org.eclipse.jetty.util.resource.URLResource;
import org.springframework.stereotype.Service;

/**
 * Default servlet for serving static content, but taken from classpath, instead of file system
 * 
 * @author jacekf
 */
@Service
public class NapalmStaticServlet extends DefaultServlet {

	private static final long serialVersionUID = 1L;

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.jetty.servlet.DefaultServlet#getResource(java.lang.String)
	 */
	@Override
	public Resource getResource(String pathInContext) {

		// ensure we always fetch it from the static classpath folder, regardless whether it is hooked up to the root / or the
		// /static context
		if (pathInContext.startsWith("/static/")) {
			return URLResource.newClassPathResource(pathInContext);
		} else {
			return URLResource.newClassPathResource("/static/" + pathInContext);
		}
	}
}
