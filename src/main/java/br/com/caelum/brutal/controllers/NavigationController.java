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

	@Get("/about")
	public void about() {
		ClassController<KarmaCalculator> mirrorOnKarma = new Mirror().on(KarmaCalculator.class);
		List<Field> karmaCalculatorFields = mirrorOnKarma.reflectAll().fields();
			
		for (Field field : karmaCalculatorFields) {
			include(field.getName(), mirrorOnKarma.get().field(field));
		}
		
		ClassController<PermissionRulesConstants> mirrorOnPermissions = new Mirror().on(PermissionRulesConstants.class);
		List<Field> permissionFields = mirrorOnPermissions.reflectAll().fields();
		
		for (Field field : permissionFields) {
			include(field.getName(), mirrorOnPermissions.get().field(field));
		}
			
	}
}
