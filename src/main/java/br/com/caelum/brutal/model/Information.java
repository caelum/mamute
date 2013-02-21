package br.com.caelum.brutal.model;

public interface Information {

    void moderate(User currentUser, UpdateStatus refused);

    Object getId();

    boolean isPending();
    
    User getAuthor();

}
