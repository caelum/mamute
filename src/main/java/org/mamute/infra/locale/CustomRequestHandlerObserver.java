package org.mamute.infra.locale;

import javax.enterprise.event.Event;
import javax.enterprise.event.Observes;
import javax.enterprise.inject.Specializes;
import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.jstl.core.Config;

import br.com.caelum.vraptor.controller.ControllerNotFoundHandler;
import br.com.caelum.vraptor.controller.InvalidInputHandler;
import br.com.caelum.vraptor.controller.MethodNotAllowedHandler;
import br.com.caelum.vraptor.core.InterceptorStack;
import br.com.caelum.vraptor.environment.Environment;
import br.com.caelum.vraptor.events.ControllerFound;
import br.com.caelum.vraptor.events.RequestSucceded;
import br.com.caelum.vraptor.events.VRaptorRequestStarted;
import br.com.caelum.vraptor.http.UrlToControllerTranslator;
import br.com.caelum.vraptor.observer.RequestHandlerObserver;

@Specializes
public class CustomRequestHandlerObserver extends RequestHandlerObserver {

	@Inject
	private Environment environment;

	@Inject
	public CustomRequestHandlerObserver(UrlToControllerTranslator translator, ControllerNotFoundHandler controllerNotFoundHandler,
										MethodNotAllowedHandler methodNotAllowedHandler, Event<ControllerFound> controllerFoundEvent,
										Event<RequestSucceded> endRequestEvent,	InterceptorStack interceptorStack, InvalidInputHandler invalidInputHandler) {
		super(translator, controllerNotFoundHandler, methodNotAllowedHandler, controllerFoundEvent,
				endRequestEvent, interceptorStack, invalidInputHandler);
	}

	@Override
	public void handle(@Observes VRaptorRequestStarted requestStarted) {
		String locale = environment.get("locale", "en");
		
		HttpServletRequest request = requestStarted.getRequest();
		request.setAttribute("javax.servlet.jsp.jstl.fmt.fallbackLocale.request", locale);
		request.setAttribute("javax.servlet.jsp.jstl.fmt.locale.request", locale);
		super.handle(requestStarted);
	}
}
