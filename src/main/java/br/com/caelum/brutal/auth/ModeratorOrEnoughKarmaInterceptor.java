package br.com.caelum.brutal.auth;

import static br.com.caelum.vraptor.view.Results.http;
import br.com.caelum.brutal.auth.rules.ComposedRule;
import br.com.caelum.brutal.auth.rules.MinimumKarmaRule;
import br.com.caelum.brutal.auth.rules.ModeratorRule;
import br.com.caelum.brutal.auth.rules.PermissionRule;
import br.com.caelum.brutal.model.LoggedUser;
import br.com.caelum.vraptor.InterceptionException;
import br.com.caelum.vraptor.Intercepts;
import br.com.caelum.vraptor.Result;
import br.com.caelum.vraptor.core.InterceptorStack;
import br.com.caelum.vraptor.interceptor.Interceptor;
import br.com.caelum.vraptor.ioc.Component;
import br.com.caelum.vraptor.resource.ResourceMethod;

@Intercepts
@Component
public class ModeratorOrEnoughKarmaInterceptor implements Interceptor {
    
    private final Result result;
	private final LoggedUser currentUser;
    
    public ModeratorOrEnoughKarmaInterceptor(LoggedUser currentUser, Result result) {
        this.currentUser = currentUser;
		this.result = result;
    }

    @Override
    public boolean accepts(ResourceMethod method) {
        return method.containsAnnotation(ModeratorOrKarmaAccess.class);
    }

    @Override
    public void intercept(InterceptorStack stack, ResourceMethod method, Object obj)
            throws InterceptionException {
    	long karma = method.getMethod().getAnnotation(ModeratorOrKarmaAccess.class).value();
    	
    	MinimumKarmaRule<Void> minimumKarmaRule = new MinimumKarmaRule<>(karma);
    	ModeratorRule<Void> moderatorRule = new ModeratorRule<>();
    	ComposedRule<Void> compose = new ComposedRule<>();
    	
    	PermissionRule<Void> rule = compose.thiz(moderatorRule).or(minimumKarmaRule);
    	
        if (!currentUser.isLoggedIn() || !rule.isAllowed(currentUser.getCurrent(), null)){
            result.use(http()).sendError(403);
            return;
        }
        stack.next(method, obj);
    }

}
