package br.com.caelum.brutal.model.interfaces;

import org.joda.time.DateTime;

import br.com.caelum.brutal.model.User;

public interface Touchable {
	DateTime getLastUpdatedAt();
	User getLastTouchedBy();
	DateTime getCreatedAt();
	User getAuthor();
}
