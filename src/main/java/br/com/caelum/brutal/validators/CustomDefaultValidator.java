package br.com.caelum.brutal.validators;

import java.util.ArrayList;
import java.util.List;

import br.com.caelum.vraptor.Result;
import br.com.caelum.vraptor.core.Localization;
import br.com.caelum.vraptor.ioc.Component;
import br.com.caelum.vraptor.proxy.Proxifier;
import br.com.caelum.vraptor.validator.BeanValidator;
import br.com.caelum.vraptor.validator.DefaultValidator;
import br.com.caelum.vraptor.validator.Outjector;
import br.com.caelum.vraptor.view.ValidationViewsFactory;

@Component
public class CustomDefaultValidator extends DefaultValidator{

	public CustomDefaultValidator(Result result,
			ValidationViewsFactory factory, Outjector outjector,
			Proxifier proxifier, CustomJSR303Validator customBeanValidator,
			Localization localization) {
		super(result, factory, outjector, proxifier, list(customBeanValidator), localization);
	}

	private static List<BeanValidator> list(CustomJSR303Validator customBeanValidator) {
		List<BeanValidator> r = new ArrayList<>();
		r.add(customBeanValidator);
		return r;
	}
	
}
