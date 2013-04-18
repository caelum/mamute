package br.com.caelum.brutal.migration;

import java.util.List;

public interface Migration {
	public List<RawSQLMigration> up();
	public List<RawSQLMigration> down();
}
