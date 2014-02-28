package org.mamute.model.interfaces;

import java.io.Serializable;

import org.mamute.model.Flag;
import org.mamute.model.User;

public interface Flaggable {

	public void add(Flag flag);
	public boolean alreadyFlaggedBy(User user);
	public void remove();
	public boolean isVisible();
	public boolean isVisibleForModeratorAndNotAuthor(User user);
	public Serializable getId();
	
}
