package br.com.caelum.brutal.template;

import br.com.caelum.brutal.controllers.TemplatesController;
import br.com.caelum.vraptor.http.FormatResolver;
import br.com.caelum.vraptor.ioc.Component;
import br.com.caelum.vraptor.resource.ResourceMethod;
import br.com.caelum.vraptor.view.DefaultPathResolver;

@Component
public class TemplatePathResolver extends DefaultPathResolver{


	public TemplatePathResolver(FormatResolver resolver) {
		super(resolver);
	}
	
	@Override
	public String pathFor(ResourceMethod method) {
		String pathFor = super.pathFor(method);
		if(method.getResource().getType().isAssignableFrom(TemplatesController.class)){
			pathFor+="f";//jspf
		}
		return pathFor;
	}
	
	
}
