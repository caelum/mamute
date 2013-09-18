package br.com.caelum.brutal.controllers;

import br.com.caelum.vraptor.Controller;
import br.com.caelum.vraptor.Get;

@Controller
public class NavigationController extends BaseController{

	@Get("/sobre")
	public void about() {}
}
