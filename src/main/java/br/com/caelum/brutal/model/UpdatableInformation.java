package br.com.caelum.brutal.model;

public interface UpdatableInformation {

    void moderate(User currentUser, UpdateStatus refused);

    Object getId();

}
