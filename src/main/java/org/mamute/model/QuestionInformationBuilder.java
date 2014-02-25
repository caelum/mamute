package org.mamute.model;

import static java.util.Arrays.asList;

import java.util.ArrayList;
import java.util.List;

public class QuestionInformationBuilder {

	private User author = null;
	private String title = "default title default title";
	private String description = "default description default description default description";
	private String comment = "new question";
	private List<Tag> tags = new ArrayList<>();
    private UpdateStatus status;

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

	public QuestionInformationBuilder withDescription(String description) {
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
		title = null;
		description = null;
		comment = null;
		tags = new ArrayList<>();
	    status = null;
	}
}
