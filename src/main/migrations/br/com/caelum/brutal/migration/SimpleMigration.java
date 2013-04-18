package br.com.caelum.brutal.migration;

import static java.util.Arrays.asList;

import java.util.Arrays;
import java.util.List;

public abstract class SimpleMigration implements Migration {
	public static final String SQL_SPLIT = "#---";

	@Override
	public List<RawSQLMigration> up() {
		return Arrays.<RawSQLMigration>asList(new OnlyUpSQLRawMigration(rawQuery()));
	}
	
	public List<RawSQLMigration> down() {
		return asList(); 
	}
	
	public boolean hasDown() {
		return !down().equals("");
	}
	
	public abstract String rawQuery();
}
