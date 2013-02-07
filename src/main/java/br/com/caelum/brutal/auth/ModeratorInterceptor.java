package br.com.caelum.brutal.auth;

import static br.com.caelum.vraptor.view.Results.http;
import br.com.caelum.brutal.model.CurrentUser;
import br.com.caelum.vraptor.InterceptionException;
import br.com.caelum.vraptor.Intercepts;
import br.com.caelum.vraptor.Result;
import br.com.caelum.vraptor.core.InterceptorStack;
import br.com.caelum.vraptor.interceptor.Interceptor;
import br.com.caelum.vraptor.ioc.Component;
import br.com.caelum.vraptor.resource.ResourceMethod;

@Intercepts
@Component
public class ModeratorInterceptor implements Interceptor {
    
    private final CurrentUser currentUser;
    private final Result result;
    
    public ModeratorInterceptor(CurrentUser currentUser, Result result) {
        this.currentUser = currentUser;
        this.result = result;
    }

    @Override
    public boolean accepts(ResourceMethod method) {
        return method.containsAnnotation(ModeratorAccess.class);
    }

    @Override
    public void intercept(InterceptorStack stack, ResourceMethod method, Object obj)
            throws InterceptionException {
        if (!currentUser.isModerator()) {
            result.use(http()).sendError(403);
        } else {
            stack.next(method, obj);
        }
    }

}
