package br.com.caelum.brutal.model;

import java.util.ArrayList;
import java.util.List;

public class QuestionInformationBuilder {

	private User author = null;
	private String title = "default title default title";
	private String description = "default description default description default description";
	private List<Tag> tags = new ArrayList<>();

	public QuestionInformationBuilder withAuthor(User author) {
		this.author = author;
		return this;
	}

	public QuestionInformation build() {
		return new QuestionInformation(title, description, new CurrentUser(author, null), tags);
	}

	public QuestionInformationBuilder withTag(Tag tag) {
		tags.add(tag);
		return this;
	}

}
