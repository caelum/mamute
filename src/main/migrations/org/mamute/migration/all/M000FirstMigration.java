package org.mamute.migration.all;


import org.mamute.migration.SimpleSchemaMigration;

import javax.enterprise.context.ApplicationScoped;

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
