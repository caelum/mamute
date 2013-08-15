package br.com.caelum.brutal.template;

import javax.inject.Inject;

import br.com.caelum.brutal.controllers.BrutalTemplatesController;
import br.com.caelum.vraptor4.core.OverrideComponent;
import br.com.caelum.vraptor4.http.FormatResolver;
import br.com.caelum.vraptor4.restfulie.controller.ControllerMethod;
import br.com.caelum.vraptor4.view.DefaultPathResolver;

@OverrideComponent
public class TemplatePathResolver extends DefaultPathResolver{

	@Deprecated
	public TemplatePathResolver() {
	}
	
	@Inject
	public TemplatePathResolver(FormatResolver resolver) {
		super(resolver);
	}
	
	@Override
	public String pathFor(ControllerMethod method) {
		String pathFor = super.pathFor(method);
		if(method.getController().getType().isAssignableFrom(BrutalTemplatesController.class)){
			pathFor+="f";//jspf
		}
		return pathFor;
	}
	
	
}
