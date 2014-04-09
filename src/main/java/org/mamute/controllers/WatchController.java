package org.mamute.controllers;

import javax.inject.Inject;

import org.mamute.brutauth.auth.rules.LoggedRule;
import org.mamute.dao.WatcherDAO;
import org.mamute.infra.ModelUrlMapping;
import org.mamute.model.LoggedUser;
import org.mamute.model.User;
import org.mamute.model.interfaces.Watchable;
import org.mamute.model.watch.Watcher;

import br.com.caelum.brutauth.auth.annotations.CustomBrutauthRules;
import br.com.caelum.vraptor.Controller;
import br.com.caelum.vraptor.Post;
import br.com.caelum.vraptor.Result;
import br.com.caelum.vraptor.routes.annotation.Routed;

@Routed
@Controller
public class WatchController {

	@Inject private WatcherDAO watchers;
	@Inject private ModelUrlMapping mapping;
	@Inject private LoggedUser currentUser;
	@Inject private Result result;

	@Post
	@CustomBrutauthRules(LoggedRule.class)
	public void watch(Long watchableId, String type) {
		Watchable watchable = watchers.findWatchable(watchableId, mapping.getClassFor(type));
		User user = currentUser.getCurrent();
		Watcher watcher = new Watcher(user);
		watchers.addOrRemove(watchable, watcher);
		result.nothing();
	}
}
