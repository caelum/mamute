package br.com.caelum.brutal.controller;

import br.com.caelum.brutal.brutauth.auth.rules.ModeratorOnlyRule;
import br.com.caelum.brutal.dao.TagDAO;
import br.com.caelum.brutal.dao.TagPageDAO;
import br.com.caelum.brutal.model.Tag;
import br.com.caelum.brutal.model.TagPage;
import br.com.caelum.brutal.validators.TagPageValidator;
import br.com.caelum.brutauth.auth.annotations.CustomBrutauthRules;
import br.com.caelum.vraptor.Get;
import br.com.caelum.vraptor.Post;
import br.com.caelum.vraptor.Resource;
import br.com.caelum.vraptor.Result;

@Resource
public class TagPageController {

	private final TagDAO tags;
	private final Result result;
	private final TagPageDAO tagPages;
	private final TagPageValidator validator;

	public TagPageController(TagDAO tags, Result result, TagPageDAO tagPages,
			TagPageValidator validator) {
		this.tags = tags;
		this.result = result;
		this.tagPages = tagPages;
		this.validator = validator;
	}
	
	@Get("/tag/{tagName}/sobre/novo")
	@CustomBrutauthRules(ModeratorOnlyRule.class)
	public void tagPageForm(String tagName){
		if(validator.validateCreationWithTag(tagName)){
			result.include("tag", tags.findByName(tagName));
		}
	}

	@Get("/tag/{tagName}/sobre/editar")
	@CustomBrutauthRules(ModeratorOnlyRule.class)
	public void editTagPageForm(String tagName){
		TagPage tagPage = tagPages.findByTag(tagName);
		result.include("tagPage", tagPage);
	}
	
	@Post("/tag/{tagName}/sobre/editar")
	@CustomBrutauthRules(ModeratorOnlyRule.class)
	public void editTagPage(String tagName, String about){
		TagPage tagPage = tagPages.findByTag(tagName);
		tagPage.setAbout(about);
		if(!validator.validate(tagPage)){
			validator.onErrorRedirectTo(TagPageController.class).editTagPageForm(tagPage.getTagName());
			return;
		}
		result.redirectTo(this).showTagPage(tagName);
	}
	
	@Post("/tag/{tagName}/sobre/novo")
	@CustomBrutauthRules(ModeratorOnlyRule.class)
	public void newTagPage(String tagName, String about){
		if(!validator.validateCreationWithTag(tagName)) return;
		Tag tag = tags.findByName(tagName);
		TagPage tagPage = new TagPage(tag, about);
		if(!validator.validate(tagPage)){
			validator.onErrorRedirectTo(TagPageController.class).tagPageForm(tagPage.getTagName());
			return;
		}
		tagPages.save(tagPage);
		result.redirectTo(this).showTagPage(tagPage.getTagName());
	}
	
	@Get("/tag/{tagName}/sobre")
	public void showTagPage(String tagName){
		TagPage tagPage = tagPages.findByTag(tagName);
		result.include(tagPage);
	}
}
