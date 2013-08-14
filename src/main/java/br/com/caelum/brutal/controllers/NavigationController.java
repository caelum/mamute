package br.com.caelum.brutal.controllers;

import br.com.caelum.vraptor4.Controller;
import br.com.caelum.vraptor4.Get;

@Controller
public class NavigationController extends BaseController{

	@Get("/sobre")
	public void about() {}
}
