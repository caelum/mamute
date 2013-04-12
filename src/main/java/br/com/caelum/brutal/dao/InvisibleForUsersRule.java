package br.com.caelum.brutal.dao;

import br.com.caelum.brutal.model.User;
import br.com.caelum.vraptor.ioc.Component;

@Component
public class InvisibleForUsersRule {
	
	private boolean invisible;

	public InvisibleForUsersRule(User user) {
		this.invisible = user == null ? false : !user.isModerator();
	}
	
	public String getIsInvisibleOrNotFilter(String modelAlias){
		if(invisible){
			return "and "+modelAlias+".moderationOptions.invisible = false";
		}
		return "";
	}
}
