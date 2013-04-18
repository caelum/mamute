package br.com.caelum.brutal.migration;

import static java.util.Arrays.asList;

import java.util.Arrays;
import java.util.List;

public abstract class SimpleMigration implements Migration {
	@Override
	public List<MigrationOperation> up() {
		return Arrays.<MigrationOperation>asList(new RawSQLOperation(rawQuery()));
	}
	
	@Override
	public List<MigrationOperation> down() {
		return asList();
	}
	
	public boolean hasDown() {
		return !down().equals("");
	}
	
	public abstract String rawQuery();
}
