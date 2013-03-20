package br.com.caelum.brutal.providers;

import java.lang.reflect.Method;
import java.util.EnumSet;
import java.util.List;

import br.com.caelum.brutal.controllers.ListController;
import br.com.caelum.vraptor.environment.Environment;
import br.com.caelum.vraptor.http.MutableRequest;
import br.com.caelum.vraptor.http.route.DefaultRouter;
import br.com.caelum.vraptor.http.route.MethodNotAllowedException;
import br.com.caelum.vraptor.http.route.Route;
import br.com.caelum.vraptor.http.route.RouteBuilder;
import br.com.caelum.vraptor.http.route.Router;
import br.com.caelum.vraptor.ioc.ApplicationScoped;
import br.com.caelum.vraptor.ioc.Component;
import br.com.caelum.vraptor.resource.HttpMethod;
import br.com.caelum.vraptor.resource.ResourceMethod;

@Component
@ApplicationScoped
public class BrutalRouter implements Router {

	private final DefaultRouter defaultRouter;
	private final String homeUrl;

	public BrutalRouter(DefaultRouter defaultRouter, Environment env) {
		this.defaultRouter = defaultRouter;
		this.homeUrl = env.get("home.url");
	}

	@Override
	public RouteBuilder builderFor(String uri) {
		return defaultRouter.builderFor(uri);
	}

	@Override
	public void add(Route r) {
		defaultRouter.add(r);
	}

	@Override
	public ResourceMethod parse(String uri, HttpMethod method,
			MutableRequest request) throws MethodNotAllowedException {
		if (uri.equals(homeUrl)) {
			return defaultRouter.parse("/", method, request);
		}
		return defaultRouter.parse(uri, method, request);
	}

	@Override
	public EnumSet<HttpMethod> allowedMethodsFor(String uri) {
		return defaultRouter.allowedMethodsFor(uri);
	}

	@Override
	public <T> String urlFor(Class<T> type, Method method, Object... params) {
		Class<ListController> listController = ListController.class;
		try {
			if (type.equals(listController)) {
				Method homeMethod = listController.getMethod("home");
				if (method.equals(homeMethod)) {
					return homeUrl;
				}
			}
		} catch (NoSuchMethodException | SecurityException e) {
			throw new RuntimeException(e);
		}
		return defaultRouter.urlFor(type, method, params);
	}

	@Override
	public List<Route> allRoutes() {
		return defaultRouter.allRoutes();
	}

}
