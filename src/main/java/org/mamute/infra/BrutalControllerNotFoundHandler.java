package org.mamute.infra;

import java.io.IOException;

import javax.annotation.Priority;
import javax.enterprise.inject.Alternative;
import javax.inject.Inject;
import javax.interceptor.Interceptor;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;

import org.apache.log4j.Logger;
import org.mamute.providers.DefaultViewObjects;

import br.com.caelum.vraptor.InterceptionException;
import br.com.caelum.vraptor.controller.ControllerNotFoundHandler;
import br.com.caelum.vraptor.http.MutableRequest;
import br.com.caelum.vraptor.http.MutableResponse;

@Alternative
@Priority(Interceptor.Priority.APPLICATION)
public class BrutalControllerNotFoundHandler implements ControllerNotFoundHandler {
	
	@Inject private DefaultViewObjects defaultViewObjects;

	private static final Logger LOG = Logger.getLogger(BrutalControllerNotFoundHandler.class);
	
	@Override
	public void couldntFind(FilterChain chain, MutableRequest request, MutableResponse response) {
		defaultViewObjects.include();
		LOG.warn("Got 404 at url:" + request.getRequestedUri());
		try {
			chain.doFilter(request, response);
		} catch (IOException e) {
			throw new InterceptionException(e);
		} catch (ServletException e) {
			throw new InterceptionException(e);
		}
	}

}
