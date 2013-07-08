package br.com.caelum.brutal.dao;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.joda.time.DateTime;

import br.com.caelum.brutal.model.Tag;
import br.com.caelum.brutal.model.TagUsage;
import br.com.caelum.brutal.model.User;
import br.com.caelum.vraptor.ioc.Component;

@Component
public class TagDAO {

	private final Session session;

	public TagDAO(Session session) {
		this.session = session;
	}
	
	public Tag findByName(String name) {
		Tag tag = (Tag) session.createQuery("from Tag t where t.name=:name").setString("name", name).uniqueResult();
		return tag;
	}

	@SuppressWarnings("unchecked")
	public List<Tag> findTagsLike(String tagChunk) {
		Query query = session.createQuery("select tag from Question question " +
				"right join question.information.tags tag " +
				"where tag.name like :tagChunk group by tag order by tag.usageCount desc");
		query.setString("tagChunk", "%"+tagChunk+"%");
		return query.list();
	}

	@SuppressWarnings("unchecked")
	public List<TagUsage> getRecentTagsSince(DateTime since) {
		Query query = session.createQuery("select new br.com.caelum.brutal.model.TagUsage(tag, count(question)) from Question question " +
				"join question.information.tags tag " +
				"where question.lastUpdatedAt > :since  group by tag order by count(question) desc");
		query.setParameter("since", since);
		return query.setMaxResults(75).list();
	}
	
	@SuppressWarnings("unchecked")
	public List<TagUsage> findMainTagsOfUser(User user) {
		Query query = session.createQuery("select new br.com.caelum.brutal.model.TagUsage(tag, count(question)) from Answer answer " +
				"join answer.question question " +
				"join question.information.tags tag " +
				"where answer.author = :user " +
				"group by tag order by count(question) desc");
		query.setParameter("user", user);
		return query.list();
	}

	@SuppressWarnings("unchecked")
	public List<Tag> all() {
		Query query = session.createQuery("from Tag tag order by tag.usageCount desc");
		return query.list();
	}

	@SuppressWarnings("unchecked")
	public List<Tag> findAllDistinct(List<String> names) {
		if (names.isEmpty())
			return new ArrayList<Tag>();
		Query query = session.createQuery("select distinct tag from Tag tag where tag.name in (:listNames)").setParameterList("listNames", names);
		return query.list();
	}

	@SuppressWarnings("unchecked")
	public List<String> allNames() {
		return session.createQuery("select t.name from Tag t").list();
	}

	public void save(Tag tag) {
		session.save(tag);
	}
}
