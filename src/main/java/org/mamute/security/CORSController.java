package org.mamute.security;

import br.com.caelum.vraptor.Controller;
import br.com.caelum.vraptor.Options;
import br.com.caelum.vraptor.Result;
import br.com.caelum.vraptor.controller.HttpMethod;
import br.com.caelum.vraptor.http.MutableRequest;
import br.com.caelum.vraptor.http.route.Router;
import static br.com.caelum.vraptor.view.Results.status;
import java.util.Set;
import javax.inject.Inject;

/**
 * Created by felipeweb on 2/12/16.
 */
@Controller
public class CORSController {
	private final Result result;
	private final Router router;
	private final MutableRequest request;

	/**
	 * @deprecated CDI eyes only
	 */
	protected CORSController() {
		this(null, null, null);
	}

	@Inject
	public CORSController(Result result, Router router, MutableRequest request) {
		this.result = result;
		this.router = router;
		this.request = request;
	}

	@Options("/*")
	public void options() {
		Set<HttpMethod> allowed = router.allowedMethodsFor(request.getRequestedUri());
		String allowMethods = allowed.toString().replaceAll("\\[|\\]", "");

		result.use(status()).header("Allow", allowMethods);
		result.use(status()).header("Access-Control-Allow-Methods", allowMethods);
		result.use(status()).header("Access-Control-Allow-Headers", "Content-Type, X-Requested-With, accept, Authorization, origin");
		result.use(status()).noContent();
	}

}

