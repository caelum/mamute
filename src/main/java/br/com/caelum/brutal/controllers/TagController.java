package br.com.caelum.brutal.controllers;

import static br.com.caelum.brutal.util.TagsSplitter.splitTags;
import static br.com.caelum.vraptor4.view.Results.json;

import java.util.List;

import javax.inject.Inject;

import br.com.caelum.brutal.brutauth.auth.rules.ModeratorOnlyRule;
import br.com.caelum.brutal.dao.TagDAO;
import br.com.caelum.brutal.model.Tag;
import br.com.caelum.brutauth.auth.annotations.CustomBrutauthRules;
import br.com.caelum.vraptor4.Controller;
import br.com.caelum.vraptor4.Get;
import br.com.caelum.vraptor4.Post;
import br.com.caelum.vraptor4.Result;

@Controller
public class TagController {

	@Inject private Result result;
	@Inject private TagDAO tags;

	
	@Get("/tags-similares/{tagChunk}")
	public void getTagsLike(String tagChunk){
		List<Tag> suggestions = tags.findTagsLike(tagChunk);
		result.use(json()).withoutRoot().from(suggestions).serialize();
	}
	
	@Post("/tags/as6nj8f8n4aju1w2nj3u1rn5a/{stringTags}")
	@CustomBrutauthRules(ModeratorOnlyRule.class)
	public void saveTags(String stringTags){
		List<String> tagList = splitTags(stringTags);
		for (String tag : tagList) {
			if(tags.findByName(tag) == null){
				tags.save(new Tag(tag, "", null));
			}
		}
		result.nothing();
	}
	
}
