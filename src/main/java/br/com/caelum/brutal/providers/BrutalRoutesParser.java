package br.com.caelum.brutal.providers;

import java.lang.reflect.Method;
import java.util.ArrayList;

import javax.annotation.Priority;
import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Alternative;
import javax.inject.Inject;
import javax.interceptor.Interceptor;

import org.apache.log4j.Logger;

import br.com.caelum.brutal.controllers.ListController;
import br.com.caelum.vraptor.environment.Environment;
import br.com.caelum.vraptor4.http.route.PathAnnotationRoutesParser;
import br.com.caelum.vraptor4.http.route.Router;

@ApplicationScoped
@Alternative
@Priority(Interceptor.Priority.APPLICATION)
public class BrutalRoutesParser extends PathAnnotationRoutesParser {
	private static final Logger LOG = Logger.getLogger(BrutalRoutesParser.class);

	private Class<ListController> homeClazz;
	private Method homeMethod;
	private String homePath;
	private boolean shouldHack = false;
	
	@Deprecated
	public BrutalRoutesParser() {
	}

	@Inject
	public BrutalRoutesParser(Router router, Environment env) {
		super(router);
		String hack = env.get("use.routes.parser.hack", "false");
		if ("true".equals(hack)) {
			LOG.info("Using hacked version of the PathAnnotationRoutesParser");
			shouldHack = true;
			homeClazz = ListController.class;
			homeMethod = getHomeMethod();
			homePath = env.get("home.url", "/");
		} else {
			LOG.info("Not using hacked version of the PathAnnotationRoutesParser");
		}
	}

	private Method getHomeMethod() {
		try {
			return homeClazz.getMethod("home", Integer.class);
		} catch (NoSuchMethodException | SecurityException e) {
			throw new RuntimeException();
		}
	}

	@Override
	protected String[] getURIsFor(Method javaMethod, Class<?> type) {
		if (shouldHack && type.equals(homeClazz) && javaMethod.equals(homeMethod)) {
			return new String[] {homePath}; 
		}
		return addUrlsWithSlash(super.getURIsFor(javaMethod, type));
	}

	private String[] addUrlsWithSlash(String[] urIsFor) {
		ArrayList<String> myUrls = new ArrayList<>();
		for (String uri : urIsFor) {
			myUrls.add(uri);
			myUrls.add(uri+"/");
		}
		return myUrls.toArray(new String[]{});
	}

}
