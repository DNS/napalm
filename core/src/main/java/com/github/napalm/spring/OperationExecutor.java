package com.github.napalm.spring;

import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.github.napalm.interfaces.CallableOperation;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

/**
 * Main service for map/reduce style parallelized query execution
 * 
 * @author jacekf
 * 
 */
@Service
public class OperationExecutor {

	private static final Logger LOG = LoggerFactory.getLogger(OperationExecutor.class);

	@Autowired
	@Qualifier(SpringConfiguration.NAPALM_EXECUTOR)
	private ExecutorService executor;

	/**
	 * Main query executor. If there is only one, it executes it immediately on the current thread If more than 1, it parallelizes
	 * them by submitting to the executor and waiting for return result
	 * 
	 * @param <T>
	 * @param operations
	 * @return Query ID / return value map
	 */
	public <DS extends Object, T extends Object> Map<String, T> execute(CallableOperation<DS, T>... operations) {
		Map<String, T> values = Maps.newLinkedHashMap();

		try {
			if (operations.length == 0) {
				// DO NOTHING
				LOG.debug("Noting to run.");
			} else if (operations.length == 1) {
				// run immediately
				LOG.debug("Running operation immediately on thread: {}", operations[0].getName());
				values.put(operations[0].getName(), operations[0].call());
			} else {
				// parallelize across all cores
				Map<String, Future<T>> futures = Maps.newLinkedHashMap();
				for (CallableOperation<DS, T> operation : operations) {
					LOG.debug("Parallelizing operation to executor: {}", operation.getName());
					futures.put(operation.getName(), executor.submit(operation));
				}
				// wait for all the tasks to finish
				for (Entry<String, Future<T>> e : futures.entrySet()) {
					values.put(e.getKey(), e.getValue().get());
				}
			}
		} catch (RuntimeException ex) {
			throw ex;
		} catch (Exception ex) {
			throw new RuntimeException(ex);
		}

		return values;
	}

	/**
	 * Extracts all the callable operations from an existing map and executes before returning back the same map with results Helper
	 * method to be called directly from all template providers
	 * 
	 * @param <P>
	 * @param operations
	 * @return Query ID / return value map
	 */
	@SuppressWarnings("unchecked")
	public <DS extends Object, T extends Object> Map<String, T> execute(Map<String, T> parameters) {
		List<CallableOperation<DS, T>> ops = Lists.newLinkedList();
		for (Entry<String, T> entry : parameters.entrySet()) {
			if (entry.getValue() instanceof CallableOperation<?, ?>) {
				ops.add((CallableOperation<DS, T>) entry.getValue());
			}
		}
		if (ops.size() > 0) {
			// there are callable operations to execute
			Map<String, T> values = execute((CallableOperation<DS, T>[]) ops.toArray(new CallableOperation<?, ?>[ops.size()]));
			// merge it back into the original map
			for (Entry<String, T> entry : values.entrySet()) {
				parameters.put(entry.getKey(), entry.getValue());
			}
		}

		return parameters;
	}
}
