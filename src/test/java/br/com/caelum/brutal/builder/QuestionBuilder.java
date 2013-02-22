package br.com.caelum.brutal.builder;

import java.util.ArrayList;
import java.util.List;

import br.com.caelum.brutal.model.LoggedUser;
import br.com.caelum.brutal.model.Question;
import br.com.caelum.brutal.model.QuestionInformation;
import br.com.caelum.brutal.model.Tag;
import br.com.caelum.brutal.model.User;


public class QuestionBuilder extends ModelBuilder{

	private String title = "default question title";
	private String description = "default question description";
	private User author;
	private List<Tag> tags = new ArrayList<Tag>();
	private Long id;

	public QuestionBuilder withTitle(String title){
		this.title = title;
		return this;
	}
	
	public QuestionBuilder withDescription(String description){
		this.description = description;
		return this;
	}

	public QuestionBuilder withAuthor(User author){
		this.author = author;
		return this;
	}
	
	public QuestionBuilder withTags(List<Tag> tags){
		this.tags = tags;
		return this;
	}
	
	public QuestionBuilder withId(Long id){
		this.id = id;
		return this;
	}
	
	public Question build(){
		Question q = new Question(new QuestionInformation(title, description, new LoggedUser(author, null), tags, ""), author);
		setId(q, id);
		clear();
		return q;
	}

	private void clear() {
		title = null;
		description = null;
		author = null; 
		tags = new ArrayList<Tag>(); 
		id = null;
	}
}
