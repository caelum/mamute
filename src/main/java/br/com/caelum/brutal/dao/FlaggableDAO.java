package br.com.caelum.brutal.dao;


import org.hibernate.Session;

import br.com.caelum.brutal.model.interfaces.Flaggable;
import br.com.caelum.vraptor.ioc.Component;

@Component
public class FlaggableDAO {

	private final Session session;

	public FlaggableDAO(Session session) {
		this.session = session;
	}
	
	public Flaggable getById(Long flaggableId, String flaggableType) {
		Flaggable flaggable = (Flaggable) session.createQuery("from "+flaggableType+" where id = :id")
				.setLong("id", flaggableId)
				.uniqueResult();
		return flaggable;
	}

}
