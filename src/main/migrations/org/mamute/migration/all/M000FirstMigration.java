package org.mamute.migration.all;

import javax.enterprise.context.ApplicationScoped;

import org.mamute.migration.SimpleSchemaMigration;

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
