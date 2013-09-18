package br.com.caelum.brutal.controllers;

import javax.inject.Inject;

import br.com.caelum.brutal.brutauth.auth.rules.LoggedRule;
import br.com.caelum.brutal.dao.WatcherDAO;
import br.com.caelum.brutal.infra.ModelUrlMapping;
import br.com.caelum.brutal.model.LoggedUser;
import br.com.caelum.brutal.model.User;
import br.com.caelum.brutal.model.interfaces.Watchable;
import br.com.caelum.brutal.model.watch.Watcher;
import br.com.caelum.brutauth.auth.annotations.CustomBrutauthRules;
import br.com.caelum.vraptor.Controller;
import br.com.caelum.vraptor.Post;
import br.com.caelum.vraptor.Result;

@Controller
public class WatchController {

	@Inject private WatcherDAO watchers;
	@Inject private ModelUrlMapping mapping;
	@Inject private LoggedUser currentUser;
	@Inject private Result result;

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
