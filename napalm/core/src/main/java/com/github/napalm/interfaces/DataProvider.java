package com.github.napalm.interfaces;

import java.util.List;
import java.util.concurrent.Callable;

/**
 * Common inteface for all data providers (SQL, NoSQL, etc.)
 * 
 * @author jacekf
 * 
 */
public interface DataProvider<DS, T> {

	/**
	 * Returns a callable that returns a single object
	 * 
	 * @param <T>
	 * @param queryName
	 * @param parameters
	 * @return
	 */
	public Callable<T> query(DS dataSource, String queryName, Object... parameters);

	/**
	 * Returns a callable that returns a list of objects of the same type
	 * 
	 * @param <T>
	 * @param queryName
	 * @param parameters
	 * @return
	 */
	public Callable<List<T>> queryForList(DS dataSource, String queryName, Object... parameters);

}
