package br.com.caelum.brutal.infra;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;

import org.apache.log4j.Logger;

import br.com.caelum.vraptor.InterceptionException;
import br.com.caelum.vraptor.core.RequestInfo;
import br.com.caelum.vraptor.ioc.Component;
import br.com.caelum.vraptor.resource.ResourceNotFoundHandler;

@Component
public class BrutalResourceNotFoundHandler implements ResourceNotFoundHandler {

	private static final Logger LOG = Logger.getLogger(BrutalResourceNotFoundHandler.class);
	
	@Override
	public void couldntFind(RequestInfo request) {
		LOG.warn("Got 404 at url:" + request.getRequestedUri());
		FilterChain chain = request.getChain();
		try {
			chain.doFilter(request.getRequest(), request.getResponse());
		} catch (IOException e) {
			throw new InterceptionException(e);
		} catch (ServletException e) {
			throw new InterceptionException(e);
		}
	}

}
