package br.com.caelum.brutal.controllers;

import br.com.caelum.brutal.builders.TagBuilder;
import br.com.caelum.brutal.model.Question;
import br.com.caelum.brutal.model.User;
import br.com.caelum.vraptor.Post;
import br.com.caelum.vraptor.Resource;

@Resource
public class TagController {
	
	private final TagBuilder tagBuilder;

	public TagController(TagBuilder tagBuilder) {
		this.tagBuilder = tagBuilder;
	}
	
	@Post("/question/tag/new")
	public void newTag(String name, String description, Question question, User author){
		tagBuilder.withName(name)
				  .withDescription(description)
				  .withQuestion(question)
				  .withAuthor(author)
				  .persisted();
	}
}
