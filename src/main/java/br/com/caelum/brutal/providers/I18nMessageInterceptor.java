package br.com.caelum.brutal.providers;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import br.com.caelum.vraptor.InterceptionException;
import br.com.caelum.vraptor.Intercepts;
import br.com.caelum.vraptor.Result;
import br.com.caelum.vraptor.core.InterceptorStack;
import br.com.caelum.vraptor.core.Localization;
import br.com.caelum.vraptor.interceptor.Interceptor;
import br.com.caelum.vraptor.ioc.Component;
import br.com.caelum.vraptor.resource.ResourceMethod;
import br.com.caelum.vraptor.validator.I18nMessage;

@Component
@Intercepts
public class I18nMessageInterceptor implements Interceptor{

	private HttpServletRequest req;
	private Localization localization;
	private Result result;

	public I18nMessageInterceptor(Localization localization, HttpServletRequest req, Result result) {
		this.localization = localization;
		this.req = req;
		this.result = result;
	}
	
	@Override
	public void intercept(InterceptorStack stack, ResourceMethod method,
			Object instance) throws InterceptionException {
		if (req.getAttribute("messages") != null){
			
			List<I18nMessage> confirmations = new ArrayList<>();
			List<I18nMessage> alerts = new ArrayList<>();
			List<I18nMessage> otherMessages = new ArrayList<>();
			
			for (I18nMessage message : (List<I18nMessage>) req.getAttribute("messages")) {
				message.setBundle(localization.getBundle());
				if (message.getCategory().equals("confirmation")){
					confirmations.add(message);
				} else if (message.getCategory().equals("alert")){
					alerts.add(message);
				} else {
					otherMessages.add(message);
				}
			}
			
			req.removeAttribute("messages");
			result.include("confirmations", confirmations);
			result.include("alerts", alerts);
			result.include("otherMessages", otherMessages);
		}
		stack.next(method, instance);
	}

	@Override
	public boolean accepts(ResourceMethod method) {
		return true;
	}

}
