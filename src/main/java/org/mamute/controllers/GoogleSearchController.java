package org.mamute.controllers;

import javax.inject.Inject;

import org.mamute.environment.EnvironmentDependent;
import org.mamute.sanitizer.HtmlSanitizer;

import br.com.caelum.vraptor.Controller;
import br.com.caelum.vraptor.Get;
import br.com.caelum.vraptor.Result;
import br.com.caelum.vraptor.environment.Environment;
import br.com.caelum.vraptor.routes.annotation.Routed;

@Controller
@EnvironmentDependent(supports="feature.google_search")
@Routed
public class GoogleSearchController {

	@Inject
	private Result result;
	@Inject
	private Environment env;
	@Inject
	private HtmlSanitizer sanitizer;
	
	@Get
	public void search(String query) {
		result.include("query", sanitizer.sanitize(query));
		result.include("customGoogleSearchKey", env.get("custom_google_search_key"));
	}
}
