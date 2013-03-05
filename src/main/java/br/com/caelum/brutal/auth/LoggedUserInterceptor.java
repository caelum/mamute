package br.com.caelum.brutal.auth;

import java.util.Arrays;

import javax.servlet.http.HttpServletRequest;

import br.com.caelum.brutal.controllers.AuthController;
import br.com.caelum.brutal.factory.MessageFactory;
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
	private MessageFactory messageFactory;
    
    public LoggedUserInterceptor(User currentUser, Result result, HttpServletRequest req, MessageFactory messageFactory) {
        this.currentUser = currentUser;
        this.result = result;
		this.req = req;
		this.messageFactory = messageFactory;
    }

    @Override
    public boolean accepts(ResourceMethod method) {
        return method.containsAnnotation(LoggedAccess.class);
    }

    @Override
    public void intercept(InterceptorStack stack, ResourceMethod method, Object instance)
            throws InterceptionException {
        if (currentUser == null) {
            result.include("messages", Arrays.asList(
            		messageFactory.build("alert","auth.access.denied")
            		));
            result.include("redirectUrl", req.getRequestURL().toString());
            result.redirectTo(AuthController.class).loginForm();
        } else {
            stack.next(method, instance);
        }
        
    }

}
