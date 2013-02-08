package br.com.caelum.brutal.model;

import org.joda.time.DateTime;

public interface Touchable {
	DateTime getLastUpdatedAt();
	User getLastTouchedBy();
	DateTime getCreatedAt();
	User getAuthor();
}
