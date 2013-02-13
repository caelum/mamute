package br.com.caelum.brutal.model;

import java.io.Serializable;


public interface Updatable extends Identifiable {

    User getAuthor();

    String getTypeName();

    Serializable getId();

    UpdateStatus approve(Information approved, User currentUser);

}
