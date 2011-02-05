package com.github.napalm.spring.query;

import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;

import org.javatuples.Pair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.github.napalm.spring.SpringConfiguration;
import com.github.napalm.utils.QueryUtils;
import com.google.common.collect.Maps;

/**
 * Main service for map/reduce style parallelized query execution
 * 
 * @author jacekf
 * 
 */
@Service
public class QueryExecutor {

	private static final Logger LOG = LoggerFactory.getLogger(QueryExecutor.class);

	@Autowired
	@Qualifier(SpringConfiguration.NAPALM_EXECUTOR)
	private ExecutorService executor;

	/**
	 * Main query executor. If there is only one, it executes it immediately on the current thread If more than 1, it parallelizes
	 * them by submitting to the executor and waiting for return result
	 * 
	 * @param <T>
	 * @param queries
	 * @return Query ID / return value map
	 */
	public <T extends Object> Map<String, T> execute(Pair<String, Callable<T>>... queries) {
		Map<String, T> values = Maps.newLinkedHashMap();

		try {
			if (queries.length == 0) {
				// DO NOTHING
				LOG.debug("Noting to run.");
			} else if (queries.length == 1) {
				// run immediately
				LOG.debug("Running query immediately on thread: {}", queries[0].getValue0());
				values.put(QueryUtils.getQueryId(queries[0].getValue0()), queries[0].getValue1().call());
			} else {
				// parallelize across all cores
				Map<String, Future<T>> futures = Maps.newLinkedHashMap();
				for (Pair<String, Callable<T>> query : queries) {
					LOG.debug("Parallelizing query to executor: {}", query.getValue0());
					futures.put(QueryUtils.getQueryId(query.getValue0()), executor.submit(query.getValue1()));
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
}
