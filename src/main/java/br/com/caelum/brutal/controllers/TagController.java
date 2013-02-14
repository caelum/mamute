package br.com.caelum.brutal.controllers;

import static br.com.caelum.vraptor.view.Results.json;

import java.util.List;

import br.com.caelum.brutal.dao.TagDAO;
import br.com.caelum.brutal.model.Tag;
import br.com.caelum.vraptor.Get;
import br.com.caelum.vraptor.Resource;
import br.com.caelum.vraptor.Result;

@Resource
public class TagController {

	private final Result result;
	private final TagDAO tags;

	public TagController(Result result, TagDAO tags) {
		this.result = result;
		this.tags = tags;
	}
	
	@Get("/tagsLike/{tagChunk}")
	public void getTagsLike(String tagChunk){
		List<Tag> suggestions = tags.findTagsLike(tagChunk);
		result.use(json()).withoutRoot().from(suggestions).serialize();
	}
}
