package org.mamute.migration;

import java.util.Arrays;
import java.util.List;

public abstract class SimpleSchemaMigration implements SchemaMigration {
	@Override
	public List<MigrationOperation> up() {
		return Arrays.<MigrationOperation>asList(new RawSQLOperation(upQuery()));
	}
	
	@Override
	public List<MigrationOperation> down() {
		return Arrays.<MigrationOperation>asList(new RawSQLOperation(downQuery()));
	}
	
	public boolean hasDown() {
		return !down().equals("");
	}
	
	public abstract String upQuery();
	public abstract String downQuery();
}
