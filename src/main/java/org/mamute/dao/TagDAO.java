package org.mamute.dao;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import br.com.caelum.vraptor.environment.Environment;
import org.hibernate.Query;
import org.hibernate.Session;
import org.joda.time.DateTime;
import org.mamute.model.Tag;
import org.mamute.model.TagUsage;
import org.mamute.model.User;

public class TagDAO {

	private Session session;

	@Deprecated
	public TagDAO() {
	}

	@Inject
	public TagDAO(Session session) {
		this.session = session;
	}
	
	public Tag findByName(String name) {
		Tag tag = (Tag) session.createQuery("from Tag t where t.name=:name").setString("name", name).uniqueResult();
		return tag;
	}

	@SuppressWarnings("unchecked")
	public List<Tag> search(String term){
		return session.createQuery("from Tag t where lower(t.name) like lower(:name)").setString("name", term+"%").list();
	}

	@SuppressWarnings("unchecked")
	public List<TagUsage> getRecentTagsSince(DateTime since, int maxResult) {
		Query query = session.createQuery("select new org.mamute.model.TagUsage(tag, count(question)) from Question question " +
				"join question.information.tags tag " +
				"where question.lastUpdatedAt > :since  group by tag order by count(question) desc, name");
		query.setParameter("since", since);
		return query.setMaxResults(maxResult).list();
	}
	
	@SuppressWarnings("unchecked")
	public List<TagUsage> findMainTagsOfUser(User user) {
		Query query = session.createQuery("select new org.mamute.model.TagUsage(tag, count(question)) from Answer answer " +
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

	public List<Tag> findAllDistinct(List<String> names) {
		if (names.isEmpty()) return new ArrayList<Tag>();
		
		ArrayList<Tag> tags = new ArrayList<>();
		for (String name : names) {
			Tag tag = (Tag) session.createQuery("from Tag where lower(name) like lower(:name)")
									.setParameter("name", name)
									.uniqueResult();
			if (tag != null && !tags.contains(tag)) {
				tags.add(tag);
			}
			
		}
		return tags;
	}

	@SuppressWarnings("unchecked")
	public List<String> allNames() {
		return session.createQuery("select t.name from Tag t").list();
	}

	public boolean hasAbout(Tag tag) {
		List<?> list = session.createQuery("select page from TagPage page where page.tag = :tag").setParameter("tag", tag).list();
		return !list.isEmpty();
	}

	public Tag saveIfDoesntExists(Tag newTag) {
		Tag existingTag = findByName(newTag.getName());
		if (existingTag == null){
			session.save(newTag);
			return newTag;
		}
		return existingTag;
	}
}
