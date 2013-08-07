package br.com.caelum.brutal.migration;

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
