package com.fico.ps.cache;

import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import org.springframework.cache.Cache;
import org.springframework.cache.support.SimpleValueWrapper;

import com.hazelcast.core.OperationTimeoutException;
import com.hazelcast.internal.util.ExceptionUtil;
import com.hazelcast.map.IMap;
import com.hazelcast.nio.ObjectDataInput;
import com.hazelcast.nio.ObjectDataOutput;
import com.hazelcast.nio.serialization.DataSerializable;

/**
 * @author chriswayjones
 * This is a copy of the Spring-Hazelcast implementation with additional logging.
 */
public class HazelcastCache implements Cache {

	private static final DataSerializable NULL = new NullDataSerializable();

	private final IMap<Object, Object> map;

	private long readTimeout;

	public HazelcastCache(IMap<Object, Object> map) {
		this.map = map;
	}

	@Override
	public String getName() {
		return this.map.getName();
	}

	@Override
	public IMap<Object, Object> getNativeCache() {
		return this.map;
	}

	@Override
	public Cache.ValueWrapper get(Object key) {

		if (key == null)
			return null;
		Object value = lookup(key);
		return (value != null) ? (Cache.ValueWrapper) new SimpleValueWrapper(fromStoreValue(value)) : null;
	}

	@Override
	public <T> T get(Object key, Class<T> type) {
		Object value = fromStoreValue(lookup(key));
		if (type != null && value != null && !type.isInstance(value))
			throw new IllegalStateException("Cached value is not of required type [" + type.getName() + "]: " + value);

		return (T) value;
	}

	@Override
	public <T> T get(Object key, Callable<T> valueLoader) {

		Object value = lookup(key);
		if (value != null)
			return (T) fromStoreValue(value);

		this.map.lock(key);
		try {
			value = lookup(key);
			if (value != null)
				return (T) fromStoreValue(value);
			return (T) loadValue(key, (Callable) valueLoader);
		} finally {
			this.map.unlock(key);
		}
	}

	private <T> T loadValue(Object key, Callable<T> valueLoader) {
		T value;
		try {
			value = valueLoader.call();
		} catch (Exception ex) {
			throw ValueRetrievalExceptionResolver.resolveException(key, valueLoader, ex);
		}
		put(key, value);
		return value;
	}

	@Override
	public void put(Object key, Object value) {

		if (key != null)
			this.map.set(key, toStoreValue(value));

	}

	protected Object toStoreValue(Object value) {
		if (value == null)
			return NULL;
		return value;
	}

	protected Object fromStoreValue(Object value) {
		if (NULL.equals(value))
			return null;

		return value;
	}

	@Override
	public void evict(Object key) {
		if (key != null)
			this.map.delete(key);
	}

	@Override
	public void clear() {
		this.map.clear();
	}

	@Override
	public Cache.ValueWrapper putIfAbsent(Object key, Object value) {
		Object result = this.map.putIfAbsent(key, toStoreValue(value));
		return (result != null) ? (Cache.ValueWrapper) new SimpleValueWrapper(fromStoreValue(result)) : null;
	}

	private Object lookup(Object key) {

		if (this.readTimeout > 0L)
			try {
				return this.map.getAsync(key).toCompletableFuture().get(this.readTimeout, TimeUnit.MILLISECONDS);
			} catch (TimeoutException te) {
				throw new OperationTimeoutException(te.getMessage());
			} catch (InterruptedException e) {
				Thread.currentThread().interrupt();
				throw ExceptionUtil.rethrow(e);
			} catch (Exception e) {
				throw ExceptionUtil.rethrow(e);
			}

		return this.map.get(key);
	}

	static final class NullDataSerializable implements DataSerializable {
		@Override
		public void writeData(ObjectDataOutput out) {
		}

		@Override
		public void readData(ObjectDataInput in) {
		}

		@Override
		public boolean equals(Object obj) {
			return (obj != null && obj.getClass() == getClass());
		}

		@Override
		public int hashCode() {
			return 0;
		}
	}

	private static class ValueRetrievalExceptionResolver {
		static RuntimeException resolveException(Object key, Callable<?> valueLoader, Throwable ex) {
			return new Cache.ValueRetrievalException(key, valueLoader, ex);
		}
	}

	public void setReadTimeout(long readTimeout) {
		this.readTimeout = readTimeout;
	}

	public long getReadTimeout() {
		return this.readTimeout;
	}
}
