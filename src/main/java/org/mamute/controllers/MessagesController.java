package org.mamute.controllers;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import javax.inject.Inject;

import br.com.caelum.vraptor.Controller;
import br.com.caelum.vraptor.Get;
import br.com.caelum.vraptor.Result;
import br.com.caelum.vraptor.environment.Environment;
import br.com.caelum.vraptor.view.Results;

@Controller
public class MessagesController {

	@Inject
	private Environment env;

	@Inject private Result result;

	@Get("/messages/loadAll")
	public void loadMessages() {
		String locale = env.get("locale", "en");

		Properties props = new Properties();
		Map<String, String> messages = new HashMap<String, String>();
		try {
			props.load(this.getClass().getResourceAsStream("/messages.properties"));
			Set<Object> keySet = props.keySet();
			for (Object key : keySet) {
				String value = (String) props.get(key);
				messages.put((String) key, value);
			}

			if (!locale.equals("en")) {
				props.load(this.getClass().getResourceAsStream("/messages_"+locale+".properties"));
				keySet = props.keySet();
				for (Object key : keySet) {
					String value = (String) props.get(key);
					messages.put((String) key, value);
				}
			}

		} catch (IOException e) {
			result.nothing();
			return;
		}

		result.use(Results.json()).from(messages).serialize();
	}

}
