package br.com.caelum.brutal.dao;

import java.util.List;

import org.hibernate.Session;

import br.com.caelum.brutal.model.Tag;
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
		return (List<Tag>) session.createQuery("from Tag t where t.name like :name").setString("name", "%"+tagChunk+"%").setMaxResults(10).list();
	}
}
