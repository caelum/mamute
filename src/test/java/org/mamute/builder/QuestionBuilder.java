package org.mamute.builder;

import static java.util.Arrays.asList;
import static org.mamute.model.MarkedText.notMarked;

import java.util.ArrayList;
import java.util.List;

import org.mamute.model.MarkedText;
import org.mamute.model.Question;
import org.mamute.model.QuestionInformation;
import org.mamute.model.QuestionInformationBuilder;
import org.mamute.model.Tag;
import org.mamute.model.User;


public class QuestionBuilder extends ModelBuilder{

	private String title = "default title default title";
	private MarkedText description = notMarked("default description default description default description");
	private String comment = "default comment";
	private User author;
	private List<Tag> tags = new ArrayList<Tag>();
	private Long id;
	private QuestionInformationBuilder informationBuilder;

	public QuestionBuilder() {
		informationBuilder = new QuestionInformationBuilder();
	}
	
	public QuestionBuilder withTitle(String title){
		this.title = title;
		return this;
	}
	
	public QuestionBuilder withDescription(MarkedText description){
		this.description = description;
		return this;
	}
	
	public QuestionBuilder withDescription(String description){
		this.description = notMarked(description);
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
	
	public QuestionBuilder withTag(Tag tag){
		tags = asList(tag);
		return this;
	}
	
	public QuestionBuilder withId(Long id){
		this.id = id;
		return this;
	}
	
	public Question build(){
		QuestionInformation questionInformation = 
				informationBuilder.withTitle(title)
				.withDescription(description)
				.with(author)
				.withTags(tags)
				.withComment(comment)
				.build();
		Question q = new Question(questionInformation, author);
		setId(q, id);
		clear();
		return q;
	}

	private void clear() {
		title = "default title default title";
		description = notMarked("default description default description default description");
		author = null; 
		tags = new ArrayList<Tag>(); 
		id = null;
	}
}
