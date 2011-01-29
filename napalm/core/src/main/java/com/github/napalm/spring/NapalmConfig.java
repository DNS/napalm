package com.github.napalm.spring;

import javax.tools.JavaCompiler;
import javax.tools.ToolProvider;

import org.springframework.stereotype.Service;

/**
 * Napalm configuration
 * @author jacekf
 *
 */
@Service
public class NapalmConfig {

	public static final String PROP_NAPALM_DEV = "napalm.dev";
	
	/**
	 * Identifies if Napalm is running in DEV or PROD mode
	 * DEV is returned if running in JDK (vs JRE) or the 'napalm.dev' property is set to 'true'
	 */
	public boolean isInDevelopmentMode() {
		//check for napalm.dev property
		String dev = System.getProperty(PROP_NAPALM_DEV);
		if (dev != null) {
			return Boolean.parseBoolean(dev);
		}
		
		//check if JDK compiler present
		JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
		if (compiler != null) {
			return true;
		}
			
		return false;
	}
	
}
