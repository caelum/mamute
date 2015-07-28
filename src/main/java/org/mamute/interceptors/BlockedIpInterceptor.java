package org.mamute.interceptors;

import br.com.caelum.vraptor.InterceptionException;
import br.com.caelum.vraptor.Intercepts;
import br.com.caelum.vraptor.Result;
import br.com.caelum.vraptor.controller.ControllerMethod;
import br.com.caelum.vraptor.core.InterceptorStack;
import br.com.caelum.vraptor.interceptor.Interceptor;
import com.google.common.base.Function;
import com.google.common.collect.Collections2;
import org.mamute.dao.BlockedIpDao;
import org.mamute.infra.ClientIp;
import org.mamute.model.ban.BlockedIp;
import org.mamute.model.ban.IpMatcher;

import javax.annotation.Nullable;
import javax.inject.Inject;
import java.util.Collection;
import java.util.List;

import static br.com.caelum.vraptor.view.Results.http;

@Intercepts
public class BlockedIpInterceptor implements Interceptor {

	@Inject
	private BlockedIpDao blockedIps;
	@Inject
	private ClientIp clientIp;
	@Inject
	private Result result;

	private static final ExtractIp extractIp = new ExtractIp();

	@Override
	public void intercept(InterceptorStack interceptorStack, ControllerMethod controllerMethod, Object o) throws InterceptionException {
		List<BlockedIp> ips = blockedIps.list();
		Collection<IpMatcher> matchers = Collections2.transform(ips, extractIp);
		boolean isBlocked = matches(matchers);
		if (isBlocked) {
			result.use(http()).sendError(503);
			return;
		}
		interceptorStack.next(controllerMethod, o);
	}

	private boolean matches(Collection<IpMatcher> matchers) {
		for (IpMatcher matcher : matchers) {
			if (matcher.matches(clientIp.get())) {
				return true;
			}
		}
		return false;
	}

	@Override
	public boolean accepts(ControllerMethod controllerMethod) {
		return true;
	}

	private static class ExtractIp implements Function<BlockedIp, IpMatcher> {
		@Override
		public IpMatcher apply(BlockedIp input) {
			return new IpMatcher(input.getIp());
		}
	}
}
