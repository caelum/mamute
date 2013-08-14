package br.com.caelum.brutal.validators;

import java.util.ArrayList;
import java.util.List;

import br.com.caelum.vraptor4.core.Localization;
import br.com.caelum.vraptor4.proxy.Proxifier;
import br.com.caelum.vraptor4.validator.BeanValidator;
import br.com.caelum.vraptor4.validator.DefaultValidator;
import br.com.caelum.vraptor4.validator.Outjector;
import br.com.caelum.vraptor4.view.ValidationViewsFactory;

@Component
public class CustomDefaultValidator extends DefaultValidator {

public CustomDefaultValidator(Result result,
			ValidationViewsFactory factory, Outjector outjector,
			Proxifier proxifier, BeanValidator beanValidator,
			Localization localization) {
		super(result, factory, outjector, proxifier, beanValidator, localization);
	}

	private static List<BeanValidator> list(CustomJSR303Validator customBeanValidator) {
		List<BeanValidator> r = new ArrayList<>();
		r.add(customBeanValidator);
		return r;
	}
	
}
