package br.com.caelum.brutal.auth.rules;

import javax.annotation.Nullable;

import br.com.caelum.brutal.model.User;
import br.com.caelum.brutal.model.interfaces.Moderatable;
import br.com.caelum.vraptor.ioc.Component;

@Component
public class AuthorizationSystem {

	private User user;

	public AuthorizationSystem(@Nullable User user) {
		this.user = user;
	}
	
	/**
	 * @throws UnauthorizedException if the user isn't allowed to edit this moderatable
	 */
	public boolean authorize(Moderatable question, Rules rules) {
		if (rules.build().isAllowed(user, question)) {
			return true;
		}
		throw new UnauthorizedException("you are not authorized to do that"); 
	}
	
}
