package br.com.caelum.brutal.model.interfaces;

public interface ViewCountable {
	/**
	 * called when the post is visited
	 */
	void ping();
	
	Long getId();


}
