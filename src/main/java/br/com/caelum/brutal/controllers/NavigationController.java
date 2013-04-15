package br.com.caelum.brutal.controllers;

import br.com.caelum.vraptor.Get;
import br.com.caelum.vraptor.Resource;

@Resource
public class NavigationController extends Controller{

	@Get("/sobre")
	public void about() {}
}
