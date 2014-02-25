package org.mamute.model.interfaces;

import java.util.List;

import org.mamute.model.watch.Watcher;

public interface Watchable {
	void add(Watcher watcher);
	void remove(Watcher watcher);
	List<Watcher> getWatchers();
	Class<?> getType();
	String getTitle();
}
