package br.com.caelum.brutal.dao;

import org.hibernate.Session;

import br.com.caelum.brutal.model.Flag;
import br.com.caelum.vraptor.ioc.Component;

@Component
public class FlagDao {

	private final Session session;

	public FlagDao(Session session) {
		this.session = session;
	}
	
	public void save(Flag f) {
		session.save(f);
	}
}
