package br.com.caelum.brutal.controllers;

import br.com.caelum.brutal.brutauth.auth.annotations.CustomBrutauthRules;
import br.com.caelum.brutal.brutauth.auth.rules.LoggedRule;
import br.com.caelum.brutal.dao.WatcherDAO;
import br.com.caelum.brutal.infra.ModelUrlMapping;
import br.com.caelum.brutal.model.LoggedUser;
import br.com.caelum.brutal.model.User;
import br.com.caelum.brutal.model.interfaces.Watchable;
import br.com.caelum.brutal.model.watch.Watcher;
import br.com.caelum.vraptor.Post;
import br.com.caelum.vraptor.Resource;
import br.com.caelum.vraptor.Result;

@Resource
public class WatchController {

	private final WatcherDAO watchers;
	private final ModelUrlMapping mapping;
	private final LoggedUser currentUser;
	private final Result result;

	public WatchController(WatcherDAO watchers, ModelUrlMapping mapping, LoggedUser currentUser, Result result) {
		this.watchers = watchers;
		this.mapping = mapping;
		this.currentUser = currentUser;
		this.result = result;
	}
	
	@Post("/{type}/acompanhar/{watchableId}")
	@CustomBrutauthRules(LoggedRule.class)
	public void watch(Long watchableId, String type) {
		Watchable watchable = watchers.findWatchable(watchableId, mapping.getClassFor(type));
		User user = currentUser.getCurrent();
		Watcher watcher = new Watcher(user);
		watchers.addOrRemove(watchable, watcher);
		result.nothing();
	}
}
