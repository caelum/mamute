package br.com.caelum.brutal.model;


public interface Updatable {

	boolean update(String field, String value);
	User getAuthor();
	String getTypeName();
	Long getId();

}
