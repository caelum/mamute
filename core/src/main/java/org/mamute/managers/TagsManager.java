package org.mamute.managers;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import org.mamute.dao.TagDAO;
import org.mamute.model.LoggedUser;
import org.mamute.model.Tag;

import br.com.caelum.vraptor.environment.Environment;

public class TagsManager {

	private Environment env;
	private TagDAO tags;
	private LoggedUser user;

	@Inject
	public TagsManager(Environment env, TagDAO tags, LoggedUser user) {
		this.env = env;
		this.tags = tags;
		this.user = user;
	}
	
	public List<Tag> findOrCreate(List<String> splitedTags) {
		if("true".equals(env.get("tags.add.anyone", "false")))
			return createTags(splitedTags);
		return findTags(splitedTags);
	}
	
	private List<Tag> findTags(List<String> splitedTags) {
		return tags.findAllDistinct(splitedTags);
	}

	private List<Tag> createTags(List<String> splitedTags){
		ArrayList<Tag> newTags = new ArrayList<>();
		for (String newTag : splitedTags) {
			Tag tag = new Tag(newTag, "", user.getCurrent() );
			tags.saveIfDoesntExists(tag);
			newTags.add(tag);
		}
		return newTags;
	}

}
