package br.com.caelum.brutal.controllers;

import javax.inject.Inject;

import br.com.caelum.vraptor.environment.Environment;
import br.com.caelum.vraptor4.Controller;
import br.com.caelum.vraptor4.Get;
import br.com.caelum.vraptor4.Result;

@Controller
public class SearchController {
    
    @Inject private Result result;
	@Inject private Environment env;
    
    @Get("/buscar")
    public void search(String query) {
    	result.include("customGoogleSearchKey", env.get("custom_google_search_key"));
        result.include("query", query);
    }
}
