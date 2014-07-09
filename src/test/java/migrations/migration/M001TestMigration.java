package migrations.migration;

import org.mamute.migration.SimpleSchemaMigration;

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
