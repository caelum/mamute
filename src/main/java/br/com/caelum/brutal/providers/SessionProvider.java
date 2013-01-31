package br.com.caelum.brutal.providers;

import org.hibernate.Session;
import org.hibernate.SessionFactory;

import br.com.caelum.vraptor.ioc.Component;
import br.com.caelum.vraptor.ioc.ComponentFactory;

@Component
public class SessionProvider implements ComponentFactory<Session>{

	private final SessionFactory factory;
	

	public SessionProvider(SessionFactory factory) {
		super();
		this.factory = factory;
	}


	@Override
	public Session getInstance() {
		return this.factory.openSession();
	}

}
