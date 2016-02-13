package org.mamute.model;

import static java.util.Arrays.asList;
import static org.mamute.model.MarkedText.notMarked;

import java.util.ArrayList;
import java.util.List;

public class QuestionInformationBuilder {

	private User author;
	private String title;
	private MarkedText description;
	private String comment;
	private List<Tag> tags;
    private UpdateStatus status;
    
    public QuestionInformationBuilder() {
		clear();
    }

	public QuestionInformationBuilder with(User author) {
		this.author = author;
		return this;
	}

	public QuestionInformationBuilder withTag(Tag tag) {
		tags = asList(tag);
		return this;
	}
	
	public QuestionInformationBuilder withTitle(String title) {
	    this.title = title;
	    return this;
	}

    public QuestionInformationBuilder withStatus(UpdateStatus status) {
        this.status = status;
        return this;
    }

	public QuestionInformationBuilder withTags(List<Tag> tags) {
		this.tags = tags;
		return this;
	}

	public QuestionInformationBuilder withDescription(MarkedText description) {
		this.description = description;
		return this;
	}

	public QuestionInformationBuilder withComment(String comment) {
		this.comment = comment;
		return this;
	}
	
	public QuestionInformation build() {
		QuestionInformation questionInformation = new QuestionInformation(title, description, new LoggedUser(author, null), tags, comment);
		if (status != null)
			questionInformation.setInitStatus(status);
		clear();
		return questionInformation;
	}

	private void clear() {
		author = null;
		title = "default title default title";
		description = notMarked("default description default description default description");
		comment = "new question";
		tags = new ArrayList<>();
	    status = null;
	}
}
