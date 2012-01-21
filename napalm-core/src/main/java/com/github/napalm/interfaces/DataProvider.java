package com.github.napalm.interfaces;

import java.util.List;

/**
 * Common inteface for all data providers (SQL, NoSQL, etc.)
 * 
 * @author jacekf
 * 
 */
public interface DataProvider<DS, DT, T> {

	/**
	 * Returns a callable that returns a single object
	 * 
	 * @param <T>
	 * @param queryName
	 * @param parameters
	 * @return
	 */
	public CallableOperation<DT, T> query(DS dataSource, String queryName, Object... parameters);

	/**
	 * Returns a callable that returns a list of objects of the same type
	 * 
	 * @param <T>
	 * @param queryName
	 * @param parameters
	 * @return
	 */
	public CallableOperation<DT, List<T>> queryForList(DS dataSource, String queryName, Object... parameters);

}
