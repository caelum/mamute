package br.com.caelum.brutal.mail.action;

import br.com.caelum.brutal.model.Post;
import br.com.caelum.brutal.model.Question;
import br.com.caelum.brutal.model.interfaces.Notifiable;

public class EmailAction{
	private final Notifiable notifiable;
	private final Post post;
	private final Question question;

	public EmailAction(Notifiable notifiable, Post post, Question question) {
		this.notifiable = notifiable;
		this.post = post;
		this.question = question;
		
	}

	public Notifiable getWhat() {
		return notifiable;
	}

	public Post getWhere() {
		return post;
	}

	public Question getQuestion() {
		return question;
	}

}
