package org.mamute.controllers;

import java.util.Map;

import javax.inject.Inject;

import org.mamute.infra.locale.MessagesLoader;

import br.com.caelum.vraptor.Controller;
import br.com.caelum.vraptor.Get;
import br.com.caelum.vraptor.Result;
import br.com.caelum.vraptor.environment.Environment;
import br.com.caelum.vraptor.view.Results;

@Controller
public class MessagesController {

	@Inject
	private MessagesLoader loader;

	@Inject
	private Environment env;

	@Inject
	private Result result;

	@Get("/messages/loadAll")
	public void loadMessages() {
		String locale = env.get("locale", "en");
		Map<String, String> messages = loader.loadBy(locale);
		result.use(Results.json()).from(messages).serialize();
	}

}
