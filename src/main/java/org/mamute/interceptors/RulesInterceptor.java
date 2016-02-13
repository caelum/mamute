package org.mamute.interceptors;

import java.lang.reflect.Field;
import java.util.List;

import javax.inject.Inject;

import org.mamute.auth.rules.PermissionRules;
import org.mamute.brutauth.auth.rules.EnvironmentKarma;
import org.mamute.reputation.rules.KarmaCalculator;

import net.vidageek.mirror.dsl.ClassController;
import net.vidageek.mirror.dsl.Mirror;
import br.com.caelum.vraptor.InterceptionException;
import br.com.caelum.vraptor.Intercepts;
import br.com.caelum.vraptor.Result;
import br.com.caelum.vraptor.controller.ControllerMethod;
import br.com.caelum.vraptor.core.InterceptorStack;
import br.com.caelum.vraptor.interceptor.FlashInterceptor;
import br.com.caelum.vraptor.interceptor.Interceptor;

@Intercepts(after=FlashInterceptor.class)
public class RulesInterceptor implements Interceptor {

	@Inject private Result result;
	@Inject private EnvironmentKarma environmentKarma;
	
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
		
		PermissionRules[] rules = PermissionRules.values();
		for (PermissionRules rule : rules) {
			long karma = environmentKarma.get(rule);
			result.include(rule.name(), karma);
		}

		stack.next(method, obj);
	}
	
}
