package br.com.caelum.brutal.controllers;

import br.com.caelum.vraptor.Get;
import br.com.caelum.vraptor.Resource;
import br.com.caelum.vraptor.Result;
import br.com.caelum.vraptor.environment.Environment;

@Resource
public class SearchController {
    
    private final Result result;
	private final Environment env;
    
    public SearchController(Result result, Environment env) {
        this.result = result;
		this.env = env;
    }

    @Get("/buscar")
    public void search(String query) {
    	result.include("customGoogleSearchKey", env.get("custom_google_search_key"));
        result.include("query", query);
    }
}
