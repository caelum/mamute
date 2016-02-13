package org.mamute.infra;

import java.util.HashMap;
import java.util.Map;

@SuppressWarnings({ "unchecked", "rawtypes" })
public class ThreadLocals {

	private static final ThreadLocal<Map<Class, Object>> results = new ThreadLocal<>();

	public <T> ThreadLocals put(Class<T> type, T value) {
		results.get().put(type, value);
		return this;
	}

	public void clear() {
		results.remove();
	}

	public <T> T get(Class<T> type) {
		return (T) results.get().get(type);
	}

	public ThreadLocals reset() {
		results.set(new HashMap<Class, Object>());
		return this;
	}

}
