package br.com.caelum.brutal.model;

import java.io.Serializable;

public interface Updatable {

	boolean update(String field, String value);
	User getAuthor();
	String getTypeName();
	Serializable getId();

}
