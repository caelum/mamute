package org.mamute.providers;

import java.util.Enumeration;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;

import br.com.caelum.vraptor.InterceptionException;
import br.com.caelum.vraptor.Intercepts;
import br.com.caelum.vraptor.controller.ControllerMethod;
import br.com.caelum.vraptor.core.InterceptorStack;
import br.com.caelum.vraptor.interceptor.FlashInterceptor;
import br.com.caelum.vraptor.interceptor.Interceptor;
@Intercepts (after=FlashInterceptor.class)
public class GlobalInterceptor implements Interceptor{
	
	private static final Logger LOG = Logger.getLogger(GlobalInterceptor.class);	
	@Inject private HttpServletRequest req;
	@Inject private DefaultViewObjects viewObjects;

	public void intercept(InterceptorStack stack, ControllerMethod method,
			Object resourceInstance) throws InterceptionException {
		LOG.debug("request for: " + req.getRequestURI());
		viewObjects.include();
		
		stack.next(method, resourceInstance);
	}

	@Override
	public boolean accepts(ControllerMethod method) {
		return true;
	}

}
