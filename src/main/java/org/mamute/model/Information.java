package org.mamute.model;

import org.mamute.model.interfaces.Moderatable;

public interface Information {

    void moderate(User currentUser, UpdateStatus refused);

    Object getId();

    boolean isPending();
    
    User getAuthor();
    
    Moderatable getModeratable();
    void setModeratable(Moderatable moderatable);

    String getTypeName();
    
    boolean isBeforeCurrent();

	void setInitStatus(UpdateStatus status);
    

}
