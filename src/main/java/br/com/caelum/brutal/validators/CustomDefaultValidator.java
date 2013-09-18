package br.com.caelum.brutal.validators;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Priority;
import javax.enterprise.context.RequestScoped;
import javax.enterprise.inject.Alternative;
import javax.inject.Inject;
import javax.interceptor.Interceptor;

import br.com.caelum.vraptor.Result;
import br.com.caelum.vraptor.core.Localization;
import br.com.caelum.vraptor.proxy.Proxifier;
import br.com.caelum.vraptor.validator.BeanValidator;
import br.com.caelum.vraptor.validator.DefaultValidator;
import br.com.caelum.vraptor.validator.Outjector;
import br.com.caelum.vraptor.view.ValidationViewsFactory;

@RequestScoped
@Alternative
@Priority(Interceptor.Priority.APPLICATION)
public class CustomDefaultValidator extends DefaultValidator {

	@Deprecated
	public CustomDefaultValidator() {
	}
	
	@Inject
	public CustomDefaultValidator(Result result,
			ValidationViewsFactory factory, Outjector outjector,
			Proxifier proxifier, BeanValidator beanValidator,
			Localization localization) {
		super(result, factory, outjector, proxifier, beanValidator,
				localization);
	}

	private static List<BeanValidator> list(
			CustomJSR303Validator customBeanValidator) {
		List<BeanValidator> r = new ArrayList<>();
		r.add(customBeanValidator);
		return r;
	}

}
