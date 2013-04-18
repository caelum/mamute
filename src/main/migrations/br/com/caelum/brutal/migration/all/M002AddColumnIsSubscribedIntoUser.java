package br.com.caelum.brutal.migration.all;

import br.com.caelum.brutal.migration.SimpleMigration;
import br.com.caelum.vraptor.ioc.ApplicationScoped;
import br.com.caelum.vraptor.ioc.Component;

@Component
@ApplicationScoped
public class M002AddColumnIsSubscribedIntoUser extends SimpleMigration {

	@Override
	public String rawQuery() {
		return "alter table Users add column isSubscribed bit(1) default 1";
	}

}
