package org.mamute.dao;

import javax.inject.Inject;

import org.hibernate.Query;
import org.hibernate.Session;
import org.mamute.infra.NotFoundException;
import org.mamute.model.TagPage;

public class TagPageDAO {
	private Session session;

	@Deprecated
	public TagPageDAO() {
	}

	@Inject
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
