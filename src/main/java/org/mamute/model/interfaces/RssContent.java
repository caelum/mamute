package org.mamute.model.interfaces;

import org.joda.time.DateTime;
import org.mamute.model.User;

public interface RssContent {

	User getAuthor();

	String getTitle();

	Long getId();

	DateTime getCreatedAt();

	String getLinkPath();
	
}
