package br.com.caelum.brutal.builders;

import br.com.caelum.brutal.dao.QuestionDAO;
import br.com.caelum.brutal.dao.TagDAO;
import br.com.caelum.brutal.dao.UserDAO;
import br.com.caelum.brutal.model.Question;
import br.com.caelum.brutal.model.Tag;
import br.com.caelum.brutal.model.User;
import br.com.caelum.vraptor.ioc.Component;

@Component
public class TagBuilder {

	private String name;
	private String description;
	private Question question;
	private User author;
	private final QuestionDAO questionDAO;
	private final UserDAO userDAO;
	private final TagDAO tagDAO;
	
	public TagBuilder(TagDAO tagDAO, UserDAO userDAO, QuestionDAO questionDAO) {
		this.tagDAO = tagDAO;
		this.userDAO = userDAO;
		this.questionDAO = questionDAO;
	}

	public TagBuilder withName(String name) {
		this.name = name;
		return this;
	}
	
	public TagBuilder withDescription(String description) {
		this.description = description;
		return this;
	}
	
	public TagBuilder withQuestion(Question question) {
		this.question = question;
		return this;
	}
	
	public TagBuilder withAuthor(User author){
		this.author = author;
		return this;
	}
	
	public Tag notPersisted() {
		return build(name, description, question, author);
	}

	public Tag persisted(){
		User authorLoaded = userDAO.load(author);
		Question questionLoaded = questionDAO.load(question);
		Tag tag = build(name, description, questionLoaded, authorLoaded);
		tagDAO.save(tag);
		return tag;
	}
	
	private Tag build(String name, String description, Question question, User author){
		Tag tag = new Tag(name, description, question, author);
		clean();
		return tag;
	}

	private void clean() {
		name = null;
		question = null;
		author = null;
		description = null;
	}

}
