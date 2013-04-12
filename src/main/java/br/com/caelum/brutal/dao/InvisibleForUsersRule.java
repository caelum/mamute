package br.com.caelum.brutal.dao;

import br.com.caelum.brutal.model.User;
import br.com.caelum.vraptor.ioc.Component;

@Component
public class InvisibleForUsersRule {
	
	private final User user;

	public InvisibleForUsersRule(User user) {
		this.user = user;
	}
	
	public String getIsInvisibleOrNotFilter(String modelAlias){
		boolean invisible = user == null || !user.isModerator();
		String filter = "";
		if(invisible){
			filter = "and ("+modelAlias+".moderationOptions.invisible = false";
			if(user != null){
				filter += " or " + modelAlias + ".author.id = " + user.getId();
			}
			filter += ")";
		}
		return filter;
	}

}
