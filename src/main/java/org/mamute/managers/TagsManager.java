package org.mamute.managers;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import br.com.caelum.vraptor.validator.Validator;
import org.mamute.dao.TagDAO;
import org.mamute.factory.MessageFactory;
import org.mamute.model.LoggedUser;
import org.mamute.model.Tag;

import br.com.caelum.vraptor.environment.Environment;

public class TagsManager {

	private Environment env;
	private TagDAO tags;
	private LoggedUser user;
	private Validator validator;
	private MessageFactory messageFactory;

	@Inject
	public TagsManager(Environment env, TagDAO tags, LoggedUser user, Validator validator, MessageFactory messageFactory) {
		this.env = env;
		this.tags = tags;
		this.user = user;
		this.validator = validator;
		this.messageFactory = messageFactory;
	}

	public List<Tag> findOrCreate(List<String> splitedTags) {
		if (env.supports("feature.tags.add.anyone"))
			return createTags(splitedTags);
		return findTags(splitedTags);
	}

	private List<Tag> findTags(List<String> splitedTags) {
		return tags.findAllDistinct(splitedTags);
	}

	private List<Tag> createTags(List<String> splitedTags) {
		ArrayList<Tag> savedTags = new ArrayList<>();
		for (String newTag : splitedTags) {
			String replace = newTag.replaceAll(env.get("tags.sanitizer.regex"), "");

			if (replace.length() != 0) {
				validator.add(messageFactory.build("error", "tag.errors.illegal_char", newTag, replace));
			} else {
				Tag tag = new Tag(newTag, "", user.getCurrent());
				tag = tags.saveIfDoesntExists(tag);
				savedTags.add(tag);
			}
		}
		return savedTags;
	}

}
