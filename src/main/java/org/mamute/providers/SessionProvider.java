package org.mamute.providers;

import javax.annotation.PostConstruct;
import javax.annotation.Priority;
import javax.enterprise.context.RequestScoped;
import javax.enterprise.inject.Alternative;
import javax.enterprise.inject.Disposes;
import javax.enterprise.inject.Produces;
import javax.inject.Inject;
import javax.interceptor.Interceptor;

import org.hibernate.Session;
import org.hibernate.SessionFactory;

@Alternative
@Priority(Interceptor.Priority.APPLICATION)
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
	@RequestScoped
	public Session getInstance() {
		return session; 
	}
	
	public void destroy(@Disposes Session session) {
	    session.close();
	}
	
	

}
