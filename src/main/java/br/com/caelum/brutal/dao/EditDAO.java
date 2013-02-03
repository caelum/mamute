package br.com.caelum.brutal.dao;

import org.hibernate.Session;

import br.com.caelum.brutal.model.Updatable;
import br.com.caelum.vraptor.ioc.Component;

@Component
@SuppressWarnings("rawtypes")
public class EditDAO {

	private final Session session;

	public EditDAO(Session session) {
		this.session = session;
	}

	public Updatable load(Class type, Long id) {
		return (Updatable) session.load(type, id);
	}

}
