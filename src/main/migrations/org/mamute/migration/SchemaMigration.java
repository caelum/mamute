package org.mamute.migration;

import java.util.List;

public interface SchemaMigration {
	public List<MigrationOperation> up();
	public List<MigrationOperation> down();
}
