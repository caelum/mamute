package org.mamute.controllers;

import br.com.caelum.vraptor.Controller;
import br.com.caelum.vraptor.Get;
import br.com.caelum.vraptor.routes.annotation.Routed;

@Routed
@Controller
public class NavigationController extends BaseController{

	@Get
	public void about() {}
}
