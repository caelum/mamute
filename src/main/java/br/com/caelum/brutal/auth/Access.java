package br.com.caelum.brutal.auth;

import br.com.caelum.brutal.model.User;
import br.com.caelum.vraptor.ioc.Component;
import br.com.caelum.vraptor.ioc.ComponentFactory;
import br.com.caelum.vraptor.ioc.SessionScoped;

@Component
@SessionScoped
public class Access implements ComponentFactory<User>{
	
	private User user; 

	public User login(User user) {
		this.user = user;
		return user;
	}

	@Override
	public User getInstance() {
		return user;
	}

	public void logout() {
		this.user = null;
	}
	

}
