package org.mamute.migration.all;

import org.mamute.migration.MigrationOperation;
import org.mamute.migration.RawSQLOperation;
import org.mamute.migration.SchemaMigration;

import javax.enterprise.context.ApplicationScoped;
import java.util.List;

@ApplicationScoped
public class M009AddIpToVote implements SchemaMigration {
	@Override
	public List<MigrationOperation> up() {
		return RawSQLOperation.forSqls("alter table Vote add column ip varchar(255)");
	}

	@Override
	public List<MigrationOperation> down() {
		return RawSQLOperation.forSqls("alter table Vote drop column ip");
	}
}
