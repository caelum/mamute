package br.com.caelum.brutal.dao;

import org.hibernate.Query;
import org.hibernate.Session;

import br.com.caelum.brutal.infra.NotFoundException;
import br.com.caelum.brutal.model.TagPage;
import br.com.caelum.vraptor.ioc.Component;

@Component
public class TagPageDAO {
	private final Session session;

	public TagPageDAO(Session session) {
		this.session = session;
	}
	
	public void save(TagPage tagPage) {
		session.save(tagPage);
	}

	public TagPage findByTag(String tagName) {
		TagPage tagPage = (TagPage) byTag(tagName).uniqueResult();
		if(tagPage == null) throw new NotFoundException();
		return tagPage;
	}

	public boolean existsOfTag(String tagName) {
		return !byTag(tagName).list().isEmpty();
	}

	private Query byTag(String tagName) {
		return session.createQuery("from TagPage where tag.name = :tag")
				.setParameter("tag", tagName);
	}

}
