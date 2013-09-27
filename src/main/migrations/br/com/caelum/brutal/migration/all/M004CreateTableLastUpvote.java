package br.com.caelum.brutal.migration.all;

import java.util.List;

import javax.enterprise.context.ApplicationScoped;

import br.com.caelum.brutal.migration.MigrationOperation;
import br.com.caelum.brutal.migration.RawSQLOperation;
import br.com.caelum.brutal.migration.SchemaMigration;

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
