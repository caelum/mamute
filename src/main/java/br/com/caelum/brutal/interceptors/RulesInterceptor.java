package br.com.caelum.brutal.interceptors;

import java.lang.reflect.Field;
import java.util.List;

import net.vidageek.mirror.dsl.ClassController;
import net.vidageek.mirror.dsl.Mirror;
import br.com.caelum.brutal.auth.rules.PermissionRulesConstants;
import br.com.caelum.brutal.reputation.rules.KarmaCalculator;
import br.com.caelum.vraptor.InterceptionException;
import br.com.caelum.vraptor.Intercepts;
import br.com.caelum.vraptor.Result;
import br.com.caelum.vraptor.core.InterceptorStack;
import br.com.caelum.vraptor.interceptor.Interceptor;
import br.com.caelum.vraptor.resource.ResourceMethod;

@Intercepts
public class RulesInterceptor implements Interceptor{

private Result result;

	public RulesInterceptor(Result result) {
		this.result = result;
	}
	
	@Override
	public boolean accepts(ResourceMethod method) {
		return true;
	}

	@Override
	public void intercept(InterceptorStack stack, ResourceMethod method,
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
