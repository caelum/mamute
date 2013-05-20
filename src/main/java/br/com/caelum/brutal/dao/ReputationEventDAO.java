package br.com.caelum.brutal.dao;

import org.hibernate.Session;

import br.com.caelum.brutal.model.ReputationEvent;
import br.com.caelum.vraptor.ioc.Component;

@Component
public class ReputationEventDAO {

	private final Session session;

	public ReputationEventDAO(Session session) {
		this.session = session;
	}

	public void save(ReputationEvent reputationEvent) {
		session.save(reputationEvent);
	}

}
