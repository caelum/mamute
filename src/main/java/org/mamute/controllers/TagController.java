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
import br.com.caelum.vraptor.routes.annotation.Routed;

@Routed
@Controller
public class TagController {

	@Inject private Result result;
	@Inject private TagDAO tags;
	@Inject private TagsSplitter splitter;

	@Post
	@CustomBrutauthRules(ModeratorOnlyRule.class)
	public void saveTags(String stringTags) {
		List<String> tagList = splitter.splitTags(stringTags);
		for (String tag : tagList) {
			tags.saveIfDoesntExists(new Tag(tag, "", null));
		}
		result.nothing();
	}

	@Get
	public void jsonTags() {
		result.use(json()).withoutRoot().from(tags.all()).serialize();
	}

	@Get
	public void searchTags(String name) {
		result.use(json()).withoutRoot().from(tags.search(name)).serialize();
	}

}
