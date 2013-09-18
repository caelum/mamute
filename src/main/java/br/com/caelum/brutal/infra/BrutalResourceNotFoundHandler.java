package br.com.caelum.brutal.infra;

import java.io.IOException;

import javax.annotation.Priority;
import javax.enterprise.inject.Alternative;
import javax.inject.Inject;
import javax.interceptor.Interceptor;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;

import org.apache.log4j.Logger;

import br.com.caelum.vraptor.InterceptionException;
import br.com.caelum.vraptor.controller.ControllerNotFoundHandler;
import br.com.caelum.vraptor.core.RequestInfo;

@Alternative
@Priority(Interceptor.Priority.APPLICATION)
public class BrutalResourceNotFoundHandler implements ControllerNotFoundHandler {
	
	@Inject private MenuInfo menuInfo;

	private static final Logger LOG = Logger.getLogger(BrutalResourceNotFoundHandler.class);
	
	@Override
	public void couldntFind(RequestInfo request) {
		menuInfo.include();
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
