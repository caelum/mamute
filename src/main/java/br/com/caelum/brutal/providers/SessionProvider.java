package br.com.caelum.brutal.providers;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import org.hibernate.Session;
import org.hibernate.SessionFactory;

import sun.awt.ComponentFactory;

@Component
public class SessionProvider implements ComponentFactory<Session>{

	private final SessionFactory factory;
    private Session session;
	
	public SessionProvider(SessionFactory factory) {
		this.factory = factory;
	}
	
	@PostConstruct
	public void create() {
	    session = this.factory.openSession();
	}

	@Override
	public Session getInstance() {
		return session; 
	}
	
	@PreDestroy
	public void destroy() {
	    session.close();
	}
	
	

}
