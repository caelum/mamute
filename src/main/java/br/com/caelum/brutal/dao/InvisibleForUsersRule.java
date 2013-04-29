package br.com.caelum.brutal.dao;

import br.com.caelum.brutal.model.LoggedUser;
import br.com.caelum.vraptor.ioc.Component;

@Component
public class InvisibleForUsersRule {
	

	private final LoggedUser currentUser;

	public InvisibleForUsersRule(LoggedUser currentUser) {
		this.currentUser = currentUser;
	}
	
	public String getInvisibleOrNotFilter(String modelAlias){
		return getInvisibleOrNotFilter(modelAlias, "");
	}
	
	public String getInvisibleOrNotFilter(String modelAlias, String connective){
		boolean hasAnotherFilter = !connective.isEmpty();
		boolean invisible = !currentUser.isLoggedIn() || !currentUser.isModerator();
		String filter = "";
		if(hasAnotherFilter){
			filter += "where";
		}
		if(invisible){
			filter = whereInvisible(modelAlias);
			if(currentUser.isLoggedIn()) filter += orAuthor(modelAlias);
			filter += ") "+connective;
		}
		
		return filter;
	}
	
	private String whereInvisible(String modelAlias) {
		return "where ("+modelAlias+".moderationOptions.invisible = false";
	}

	private String orAuthor(String modelAlias) {
		return " or " + modelAlias + ".author.id = " + currentUser.getCurrent().getId();
	}


}
