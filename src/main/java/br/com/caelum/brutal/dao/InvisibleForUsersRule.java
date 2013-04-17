package br.com.caelum.brutal.dao;

import br.com.caelum.brutal.model.User;
import br.com.caelum.vraptor.ioc.Component;

@Component
public class InvisibleForUsersRule {
	
	private final User user;

	public InvisibleForUsersRule(User user) {
		this.user = user;
	}
	
	public String getInvisibleOrNotFilter(String modelAlias){
		return getInvisibleOrNotFilter(modelAlias, "");
	}
	
	public String getInvisibleOrNotFilter(String modelAlias, String connective){
		boolean hasAnotherFilter = !connective.isEmpty();
		boolean invisible = user == null || !user.isModerator();
		String filter = "";
		if(hasAnotherFilter){
			filter += "where";
		}
		if(invisible){
			filter = whereInvisible(modelAlias);
			if(user != null) filter += orAuthor(modelAlias);
			filter += ") "+connective;
		}
		
		return filter;
	}
	
	private String whereInvisible(String modelAlias) {
		return "where ("+modelAlias+".moderationOptions.invisible = false";
	}

	private String orAuthor(String modelAlias) {
		return " or " + modelAlias + ".author.id = " + user.getId();
	}


}
