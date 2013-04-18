package br.com.caelum.brutal.migration;

public class NotAValidMigration extends SimpleMigration {

	@Override
	public String rawQuery() {
		return "up";
	}

}
