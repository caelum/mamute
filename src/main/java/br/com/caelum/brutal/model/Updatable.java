package br.com.caelum.brutal.model;

import java.io.Serializable;


public interface Updatable {

    User getAuthor();

    String getTypeName();

    Serializable getId();

}
