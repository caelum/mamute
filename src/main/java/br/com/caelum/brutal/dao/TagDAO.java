package br.com.caelum.brutal.dao;

import org.hibernate.Session;

import br.com.caelum.brutal.model.Tag;
import br.com.caelum.vraptor.ioc.Component;

@Component
public class TagDAO {

	private final Session session;

	public TagDAO(Session session) {
		this.session = session;
	}
	
	public void save(Tag tag) {
	  session.save(tag);
    }

	public Tag findById(Long tagId) {
		return (Tag) session.load(Tag.class, tagId);
	}
	
}
