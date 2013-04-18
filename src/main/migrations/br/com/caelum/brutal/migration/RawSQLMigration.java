package br.com.caelum.brutal.migration;

public class RawSQLMigration {
	
	private final String upSql;
	private final String downSql;
	
	public RawSQLMigration(String upSql, String downSql) {
		this.upSql = upSql;
		this.downSql = downSql;
	}

	public String up() {
		return upSql;
	}
	
	public String down() {
		return downSql;
	}
}
