package br.com.caelum.brutal.components;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import javax.enterprise.inject.spi.BeanManager;
import javax.inject.Inject;

import org.jboss.weld.context.bound.BoundRequestContext;
import org.jboss.weld.context.bound.BoundSessionContext;

import br.com.caelum.vraptor4.core.Execution;
import br.com.caelum.vraptor4.core.RequestInfo;
import br.com.caelum.vraptor4.ioc.Container;

public class CDIFakeRequestProvider {
	
	@Inject	private BoundRequestContext requestContext;
	@Inject	private BoundSessionContext sessionContext;
	@Inject private RequestInfo requestInfo;
	@Inject	private BeanManager beanManager;
	@Inject	private Container container;

	public void insideRequest(Execution<Void> execution) {
		Map<String, Object> requestDataStore = Collections
				.synchronizedMap(new HashMap<String, Object>());
		requestContext.associate(requestDataStore);
		requestContext.activate();
		Map<String, Object> sessionDataStore = Collections
				.synchronizedMap(new HashMap<String, Object>());
		sessionContext.associate(sessionDataStore);
		sessionContext.activate();
		beanManager.fireEvent(requestInfo);
		
		execution.insideRequest(container);
		
		requestContext.invalidate();
		requestContext.deactivate();
		requestContext.dissociate(requestDataStore);
		sessionContext.invalidate();
		sessionContext.deactivate();
		sessionContext.dissociate(sessionDataStore);
	}

}
