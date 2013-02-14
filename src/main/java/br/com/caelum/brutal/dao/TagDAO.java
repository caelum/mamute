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
	
	public Tag saveOrLoad(Tag tag) {
		Tag loadedTag = findByName(tag.getName());
		if(loadedTag == null){
			save(tag);
			return tag;
		}else{
			save(loadedTag);
			return loadedTag;
		}
	}
	
	public Tag findByName(String name) {
		return (Tag) session.createQuery("from Tag t where t.name like :name").setString("name", name).uniqueResult();
	}

	private void save(Tag tag) {
		session.save(tag);
	}

	@SuppressWarnings("unchecked")
	public List<Tag> findTagsLike(String tagChunk) {
		Query query = session.createQuery("select tag from Question question " +
				"join question.information.tags tag " +
				"where tag.name like :tagChunk group by tag order by tag.usageCount desc");
		query.setString("tagChunk", "%"+tagChunk+"%");
		return query.list();
	}

	public List<Tag> loadAll(String tagNames, User author) {
		List<Tag> tags = new ArrayList<>();
		if(tagNames==null || tagNames.isEmpty()) return tags;
		for (String tagName : tagNames.split(" ")) {
			Tag newTag = saveOrLoad(new Tag(tagName, "", author));
			tags.add(newTag);
		}
		return tags;
	}

	@SuppressWarnings("unchecked")
	public List<TagUsage> getRecentTagsSince(DateTime since) {
		Query query = session.createQuery("select new br.com.caelum.brutal.model.TagUsage(tag, count(question)) from Question question " +
				"join question.information.tags tag " +
				"where question.lastUpdatedAt > :since  group by tag order by count(question) desc");
		query.setParameter("since", since);
		return query.list();
	}
}
