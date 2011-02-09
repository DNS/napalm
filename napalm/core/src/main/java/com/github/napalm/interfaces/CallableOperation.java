package com.github.napalm.interfaces;

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
public abstract class CallableOperation<DT,T> implements Callable<T> {

	private String name;
	private String value;
	private DT dataInterface;
	private Object[] parameters;

}
