package br.com.caelum.brutal.migration.all;

import br.com.caelum.brutal.migration.SimpleMigration;

//@Component
//@ApplicationScoped
public class M007DropSessionKeyColumnFromUser extends SimpleMigration {

	@Override
	public String rawQuery() {
		return "alter table User drop column sessionKey";
	}

}
