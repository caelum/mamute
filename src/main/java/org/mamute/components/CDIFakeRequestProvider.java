package org.mamute.components;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;

import org.jboss.weld.context.bound.BoundRequestContext;
import org.jboss.weld.context.bound.BoundSessionContext;

import br.com.caelum.vraptor.ioc.Container;

public class CDIFakeRequestProvider {
	
	@Inject	private BoundRequestContext requestContext;
	@Inject	private BoundSessionContext sessionContext;
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
		
		execution.insideRequest(container);
		
		requestContext.invalidate();
		requestContext.deactivate();
		requestContext.dissociate(requestDataStore);
		sessionContext.invalidate();
		sessionContext.deactivate();
		sessionContext.dissociate(sessionDataStore);
	}

}
