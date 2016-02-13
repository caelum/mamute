package org.mamute.migration.all;

import java.util.List;

import javax.enterprise.context.ApplicationScoped;

import org.mamute.migration.MigrationOperation;
import org.mamute.migration.RawSQLOperation;
import org.mamute.migration.SchemaMigration;

@ApplicationScoped
public class M005UpdateInvisibleQuestion implements SchemaMigration {
	@Override
	public List<MigrationOperation> up() {
		return RawSQLOperation.forSqls("update Question set invisible=1 where voteCount <= -5;");
	}

	@Override
	public List<MigrationOperation> down() {
		return RawSQLOperation.forSqls();
	}
}
