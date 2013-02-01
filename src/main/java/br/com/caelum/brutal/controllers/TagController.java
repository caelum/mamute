package br.com.caelum.brutal.controllers;

import br.com.caelum.brutal.dao.TagDAO;
import br.com.caelum.brutal.model.Question;
import br.com.caelum.brutal.model.Tag;
import br.com.caelum.brutal.model.User;
import br.com.caelum.vraptor.Post;
import br.com.caelum.vraptor.Resource;

@Resource
public class TagController {
	
	private final TagDAO tagDao;

	public TagController(TagDAO tagDao) {
		this.tagDao = tagDao;
	}
	
	@Post("/question/tag/new")
	public void newTag(String name, String description, Question question, User author){
		Tag tag = new Tag(name, description, question, author);
		tagDao.save(tag);
	}
}
