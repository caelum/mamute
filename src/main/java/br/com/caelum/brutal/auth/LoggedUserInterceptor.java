package br.com.caelum.brutal.auth;

import java.util.Arrays;

import javax.servlet.http.HttpServletRequest;

import br.com.caelum.brutal.controllers.AuthController;
import br.com.caelum.brutal.model.User;
import br.com.caelum.vraptor.InterceptionException;
import br.com.caelum.vraptor.Intercepts;
import br.com.caelum.vraptor.Result;
import br.com.caelum.vraptor.core.InterceptorStack;
import br.com.caelum.vraptor.interceptor.Interceptor;
import br.com.caelum.vraptor.resource.ResourceMethod;

@Intercepts
public class LoggedUserInterceptor implements Interceptor {

    private final User currentUser;
    private final Result result;
	private final HttpServletRequest req;
    
    public LoggedUserInterceptor(User currentUser, Result result, HttpServletRequest req) {
        this.currentUser = currentUser;
        this.result = result;
		this.req = req;
    }

    @Override
    public boolean accepts(ResourceMethod method) {
        return method.containsAnnotation(Logged.class);
    }

    @Override
    public void intercept(InterceptorStack stack, ResourceMethod method, Object instance)
            throws InterceptionException {
        if (currentUser == null) {
            result.include("alerts", Arrays.asList("auth.access.denied"));
            result.include("redirectUrl", req.getRequestURL().toString());
            result.redirectTo(AuthController.class).loginForm();
        } else {
            stack.next(method, instance);
        }
        
    }

}
