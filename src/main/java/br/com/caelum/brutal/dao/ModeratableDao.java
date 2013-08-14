package br.com.caelum.brutal.dao;

import org.hibernate.Session;

import br.com.caelum.brutal.model.interfaces.Moderatable;

@Component
public class ModeratableDao {
	private final Session session;
	
    public ModeratableDao(Session session) {
		this.session = session;
	}

	public Moderatable getById(Long id, Class<?> clazz) {
        return (Moderatable) session.load(clazz, id);
    }

}
