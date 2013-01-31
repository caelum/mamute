package br.com.caelum.brutal.providers;

import javax.persistence.EntityManager;

import org.hibernate.Session;

import br.com.caelum.vraptor.ioc.Component;
import br.com.caelum.vraptor.ioc.ComponentFactory;

@Component
public class SessionDelegatorCreator implements ComponentFactory<Session>{
	
	private EntityManager em;

	public SessionDelegatorCreator(EntityManager em) {
		this.em = em;
	}

	@Override
	public Session getInstance() {
		return (Session) em.getDelegate();
	}

}
