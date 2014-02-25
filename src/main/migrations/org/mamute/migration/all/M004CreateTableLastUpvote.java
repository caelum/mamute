package org.mamute.migration.all;

import java.util.List;

import javax.enterprise.context.ApplicationScoped;

import org.mamute.migration.MigrationOperation;
import org.mamute.migration.RawSQLOperation;
import org.mamute.migration.SchemaMigration;

@ApplicationScoped
public class M004CreateTableLastUpvote implements SchemaMigration {
	@Override
	public List<MigrationOperation> up() {
		return RawSQLOperation.forSqls("alter table Users add column lastUpvote datetime", "update Users set lastUpvote = now()");
	}

	@Override
	public List<MigrationOperation> down() {
		return RawSQLOperation.forSqls("alter table Users drop column lastUpvote");
	}
}
