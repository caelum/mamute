package br.com.caelum.brutal.migration.all;

import br.com.caelum.vraptor.environment.Environment;
import br.com.caelum.vraptor.ioc.ApplicationScoped;
import br.com.caelum.vraptor.ioc.Component;

@Component
@ApplicationScoped
public class M024UpdateUsersFacebookPhoto extends M022UpdateUsersFacebookPhoto {
	
	public M024UpdateUsersFacebookPhoto(Environment env) {
		super(env);
	}

}