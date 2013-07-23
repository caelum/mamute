package br.com.caelum.brutal.model.interfaces;

import org.joda.time.DateTime;

import br.com.caelum.brutal.model.User;

public interface RssContent {

	User getAuthor();

	String getTitle();

	Long getId();

	DateTime getCreatedAt();

	String getLinkPath();
	
}
