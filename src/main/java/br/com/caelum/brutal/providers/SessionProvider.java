package br.com.caelum.brutal.providers;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.enterprise.inject.Produces;
import javax.inject.Inject;

import org.hibernate.Session;
import org.hibernate.SessionFactory;

import br.com.caelum.vraptor4.core.OverrideComponent;

@OverrideComponent
public class SessionProvider {

	private SessionFactory factory;
    private Session session;

    @Deprecated
    public SessionProvider() {
	}
	
    @Inject
	public SessionProvider(SessionFactory factory) {
		this.factory = factory;
	}
	
	@PostConstruct
	public void create() {
	    session = this.factory.openSession();
	}

	@Produces
	public Session getInstance() {
		return session; 
	}
	
	@PreDestroy
	public void destroy() {
	    session.close();
	}
	
	

}
