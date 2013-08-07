package br.com.caelum.brutal.migration.all;

import br.com.caelum.brutal.migration.SimpleSchemaMigration;
import br.com.caelum.vraptor.ioc.ApplicationScoped;
import br.com.caelum.vraptor.ioc.Component;

@Component
@ApplicationScoped
public class M000FirstMigration extends SimpleSchemaMigration {

	@Override
	public String upQuery() {
		return "";
	}
	
	@Override
	public String downQuery() {
		return "";
	}

}
