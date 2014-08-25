package org.mamute.controllers;

import static org.mamute.sanitizer.HtmlSanitizer.sanitize;

import javax.inject.Inject;

import org.mamute.environment.EnvironmentDependent;

import br.com.caelum.vraptor.Controller;
import br.com.caelum.vraptor.Get;
import br.com.caelum.vraptor.Result;
import br.com.caelum.vraptor.environment.Environment;

@Controller
@EnvironmentDependent(supports="feature.google_search")
public class GoogleSearchController {

	@Inject
	private Result result;
	@Inject
	private Environment env;
	
	@Get("/search")
	public void search(String query) {
		result.include("query", sanitize(query));
		result.include("customGoogleSearchKey", env.get("custom_google_search_key"));
	}
}
