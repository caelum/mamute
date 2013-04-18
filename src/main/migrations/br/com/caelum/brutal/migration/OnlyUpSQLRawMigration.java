package br.com.caelum.brutal.migration;

public class OnlyUpSQLRawMigration extends RawSQLMigration {

	public OnlyUpSQLRawMigration(String upSql) {
		super(upSql, "");
	}
	
}
