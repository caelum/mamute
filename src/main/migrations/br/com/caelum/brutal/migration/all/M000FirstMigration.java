package br.com.caelum.brutal.migration.all;

import javax.enterprise.context.ApplicationScoped;

import br.com.caelum.brutal.migration.SimpleSchemaMigration;

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
