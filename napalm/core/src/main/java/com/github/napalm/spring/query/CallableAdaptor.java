package com.github.napalm.spring.query;

import java.util.concurrent.Callable;

import lombok.Data;

/**
 * Simplifies creating some of the complex Callable classes
 * Avoids having to use final variables, which can screw up GC under heavy load
 * 
 * @author jacekf
 * 
 */
@Data
public abstract class CallableAdaptor<DT,T> implements Callable<T> {

	private String queryValue;
	private DT dataInterface;
	private Object[] parameters;

}
