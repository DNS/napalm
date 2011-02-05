package com.github.napalm.spring.query;

import java.util.List;
import java.util.concurrent.Callable;

import org.javatuples.Pair;

/**
 * Common inteface for all query engines (SQL, NoSQL, etc.)
 * 
 * @author jacekf
 * 
 */
public interface QueryEngine<DS, T> {

	/**
	 * Returns a callable that returns a single object
	 * 
	 * @param <T>
	 * @param queryName
	 * @param parameters
	 * @return
	 */
	public Pair<String, Callable<T>> query(DS dataSource, String queryName, Object... parameters);

	/**
	 * Returns a callable that returns a list of objects of the same type
	 * 
	 * @param <T>
	 * @param queryName
	 * @param parameters
	 * @return
	 */
	public Pair<String, Callable<List<T>>> queryForList(DS dataSource, String queryName, Object... parameters);

}
