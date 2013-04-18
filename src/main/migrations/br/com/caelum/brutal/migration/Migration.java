package br.com.caelum.brutal.migration;

import java.util.List;

public interface Migration {
	public List<MigrationOperation> up();
	public List<MigrationOperation> down();
}
