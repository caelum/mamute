package org.mamute.model.interfaces;

import org.joda.time.DateTime;
import org.mamute.model.User;

public interface Touchable {
	DateTime getLastUpdatedAt();
	User getLastTouchedBy();
	DateTime getCreatedAt();
	User getAuthor();
	boolean isEdited();
}
