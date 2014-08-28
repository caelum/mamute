package org.mamute.template;

import javax.annotation.Priority;
import javax.enterprise.inject.Alternative;
import javax.inject.Inject;
import javax.interceptor.Interceptor;

import org.mamute.controllers.BrutalTemplatesController;

import br.com.caelum.vraptor.controller.ControllerMethod;
import br.com.caelum.vraptor.http.FormatResolver;
import br.com.caelum.vraptor.view.DefaultPathResolver;

@Alternative
@Priority(Interceptor.Priority.APPLICATION)
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
