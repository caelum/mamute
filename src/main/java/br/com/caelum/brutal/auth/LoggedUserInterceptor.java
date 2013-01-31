package br.com.caelum.brutal.auth;

import java.util.Arrays;

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
    
    public LoggedUserInterceptor(User currentUser, Result result) {
        this.currentUser = currentUser;
        this.result = result;
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
            result.redirectTo(AuthController.class).root();
        } else {
            stack.next(method, instance);
        }
        
    }

}
