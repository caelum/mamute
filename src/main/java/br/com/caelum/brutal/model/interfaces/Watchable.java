package br.com.caelum.brutal.model.interfaces;

import java.util.List;

import br.com.caelum.brutal.model.watch.Watcher;

public interface Watchable {
	void add(Watcher watcher);
	void remove(Watcher watcher);
	List<Watcher> getWatchers();

}
