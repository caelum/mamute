package org.mamute.migration;

import org.mamute.migration.SimpleSchemaMigration;

public class NotAValidMigration extends SimpleSchemaMigration {

	@Override
	public String upQuery() {
		return "up";
	}
	
	@Override
	public String downQuery() {
		return "down";
	}

}
