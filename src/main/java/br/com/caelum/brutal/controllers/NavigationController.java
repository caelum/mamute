package br.com.caelum.brutal.controllers;

import java.lang.reflect.Field;
import java.util.List;

import net.vidageek.mirror.dsl.ClassController;
import net.vidageek.mirror.dsl.Mirror;
import br.com.caelum.brutal.auth.rules.PermissionRulesConstants;
import br.com.caelum.brutal.reputation.rules.KarmaCalculator;
import br.com.caelum.vraptor.Get;
import br.com.caelum.vraptor.Resource;

@Resource
public class NavigationController extends Controller{

	@Get("/sobre")
	public void about() {}
}
