package br.com.caelum.brutal.model;

import br.com.caelum.brutal.model.interfaces.Moderatable;

public interface Information {

    void moderate(User currentUser, UpdateStatus refused);

    Object getId();

    boolean isPending();
    
    User getAuthor();
    
    Moderatable getModeratable();

    String getTypeName();

}
