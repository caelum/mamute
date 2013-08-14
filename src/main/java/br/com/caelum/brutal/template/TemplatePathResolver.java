package br.com.caelum.brutal.template;

import br.com.caelum.brutal.controllers.BrutalTemplatesController;
import br.com.caelum.vraptor4.http.FormatResolver;
import br.com.caelum.vraptor4.view.DefaultPathResolver;

@Component
public class TemplatePathResolver extends DefaultPathResolver{


	public TemplatePathResolver(FormatResolver resolver) {
		super(resolver);
	}
	
	@Override
	public String pathFor(ResourceMethod method) {
		String pathFor = super.pathFor(method);
		if(method.getResource().getType().isAssignableFrom(BrutalTemplatesController.class)){
			pathFor+="f";//jspf
		}
		return pathFor;
	}
	
	
}
