package br.com.caelum.brutal.model;

import java.util.ArrayList;
import java.util.List;

public class QuestionInformationBuilder {

	private User author = null;
	private String title = "default title default title";
	private String description = "default description default description default description";
	private List<Tag> tags = new ArrayList<>();
    private UpdateStatus status;

	public QuestionInformationBuilder with(User author) {
		this.author = author;
		return this;
	}

	public QuestionInformation build() {
		QuestionInformation questionInformation = new QuestionInformation(title, description, new CurrentUser(author, null), tags);
		if (status != null)
		    questionInformation.setInitStatus(status);
        return questionInformation;
	}

	public QuestionInformationBuilder withTag(Tag tag) {
		tags.add(tag);
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
		this.tags.addAll(tags);
		return this;
	}

}
