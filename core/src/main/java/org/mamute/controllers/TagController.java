package org.mamute.controllers;

import static br.com.caelum.vraptor.view.Results.json;

import java.util.List;

import javax.inject.Inject;

import org.mamute.brutauth.auth.rules.ModeratorOnlyRule;
import org.mamute.dao.TagDAO;
import org.mamute.model.Tag;
import org.mamute.util.TagsSplitter;

import br.com.caelum.brutauth.auth.annotations.CustomBrutauthRules;
import br.com.caelum.vraptor.Controller;
import br.com.caelum.vraptor.Get;
import br.com.caelum.vraptor.Post;
import br.com.caelum.vraptor.Result;
import br.com.caelum.vraptor.view.Results;

@Controller
public class TagController {

	@Inject private Result result;
	@Inject private TagDAO tags;
	@Inject private TagsSplitter splitter;

	
	@Get("/tags-similares/{tagChunk}")
	public void getTagsLike(String tagChunk){
		List<Tag> suggestions = tags.findTagsLike(tagChunk);
		result.use(Results.json()).withoutRoot().from(suggestions).serialize();
	}
	
	@Post("/tags/as6nj8f8n4aju1w2nj3u1rn5a/{stringTags}")
	@CustomBrutauthRules(ModeratorOnlyRule.class)
	public void saveTags(String stringTags){
		List<String> tagList = splitter.splitTags(stringTags);
		for (String tag : tagList) {
			tags.saveIfDoesntExists(new Tag(tag, "", null));
		}
		result.nothing();
	}
	
	@Get("/pergunta/allTags")
	public void jsonTags () {
		result.use(json()).withoutRoot().from(tags.all()).serialize();
	}
	
}
