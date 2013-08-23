package br.com.caelum.brutal.interceptors;

import java.lang.reflect.Field;
import java.util.List;

import javax.inject.Inject;

import net.vidageek.mirror.dsl.ClassController;
import net.vidageek.mirror.dsl.Mirror;
import br.com.caelum.brutal.auth.rules.PermissionRulesConstants;
import br.com.caelum.brutal.reputation.rules.KarmaCalculator;
import br.com.caelum.vraptor4.InterceptionException;
import br.com.caelum.vraptor4.Intercepts;
import br.com.caelum.vraptor4.Result;
import br.com.caelum.vraptor4.controller.ControllerMethod;
import br.com.caelum.vraptor4.core.InterceptorStack;
import br.com.caelum.vraptor4.interceptor.Interceptor;

@Intercepts
public class RulesInterceptor implements Interceptor {

	@Inject private Result result;
	
	@Override
	public boolean accepts(ControllerMethod method) {
		return true;
	}

	@Override
	public void intercept(InterceptorStack stack, ControllerMethod method,
			Object obj) throws InterceptionException {
		ClassController<KarmaCalculator> mirrorOnKarma = new Mirror().on(KarmaCalculator.class);
		List<Field> karmaCalculatorFields = mirrorOnKarma.reflectAll().fields();
			
		for (Field field : karmaCalculatorFields) {
			result.include(field.getName(), mirrorOnKarma.get().field(field));
		}
		
		ClassController<PermissionRulesConstants> mirrorOnPermissions = new Mirror().on(PermissionRulesConstants.class);
		List<Field> permissionFields = mirrorOnPermissions.reflectAll().fields();
		
		for (Field field : permissionFields) {
			result.include(field.getName(), mirrorOnPermissions.get().field(field));
		}
		
		stack.next(method, obj);
	}
	
}
