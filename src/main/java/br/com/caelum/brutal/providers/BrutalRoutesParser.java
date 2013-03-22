package br.com.caelum.brutal.providers;

import java.lang.reflect.Method;

import br.com.caelum.brutal.controllers.ListController;
import br.com.caelum.vraptor.environment.Environment;
import br.com.caelum.vraptor.http.route.PathAnnotationRoutesParser;
import br.com.caelum.vraptor.http.route.Router;
import br.com.caelum.vraptor.ioc.ApplicationScoped;
import br.com.caelum.vraptor.ioc.Component;

@Component
@ApplicationScoped
public class BrutalRoutesParser extends PathAnnotationRoutesParser {

	private final Class<ListController> homeClazz;
	private final Method homeMethod;
	private final String homePath;

	public BrutalRoutesParser(Router router, Environment env) {
		super(router);
		homeClazz = ListController.class;
		homeMethod = getHomeMethod();
		homePath = env.get("home.url");
	}

	private Method getHomeMethod() {
		try {
			return homeClazz.getMethod("home");
		} catch (NoSuchMethodException | SecurityException e) {
			throw new RuntimeException();
		}
	}

	@Override
	protected String[] getURIsFor(Method javaMethod, Class<?> type) {
		if (type.equals(homeClazz) && javaMethod.equals(homeMethod)) {
			return new String[] {homePath}; 
		}
		return super.getURIsFor(javaMethod, type);
	}

}
