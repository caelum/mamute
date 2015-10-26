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

	public TagPage findByTag(String tagSluggedName) {
		TagPage tagPage = (TagPage) byTag(tagSluggedName).uniqueResult();
		if(tagPage == null) throw new NotFoundException();
		return tagPage;
	}

	public boolean existsOfTag(String tagSluggedName) {
		return !byTag(tagSluggedName).list().isEmpty();
	}

	private Query byTag(String tagName) {
		return session.createQuery("from TagPage where tag.sluggedName = :tag")
				.setParameter("tag", tagName);
	}

}
