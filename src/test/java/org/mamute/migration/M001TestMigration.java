package org.mamute.migration;

import org.mamute.migration.SimpleSchemaMigration;

import javax.enterprise.inject.Vetoed;

@Vetoed
public class M001TestMigration extends SimpleSchemaMigration {

	@Override
	public String upQuery() {
		return "up";
	}
	
	@Override
	public String downQuery() {
		return "down";
	}

}
