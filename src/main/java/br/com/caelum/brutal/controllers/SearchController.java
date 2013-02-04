package br.com.caelum.brutal.controllers;

import br.com.caelum.vraptor.Get;
import br.com.caelum.vraptor.Resource;
import br.com.caelum.vraptor.Result;

@Resource
public class SearchController {
    
    private final Result result;
    
    public SearchController(Result result) {
        this.result = result;
    }

    @Get("/search")
    public void search(String query) {
        result.include("query", query);
    }
}
