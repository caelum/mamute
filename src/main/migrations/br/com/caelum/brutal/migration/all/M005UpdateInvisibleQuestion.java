package br.com.caelum.brutal.migration.all;

import java.util.List;

import javax.enterprise.context.ApplicationScoped;

import br.com.caelum.brutal.migration.MigrationOperation;
import br.com.caelum.brutal.migration.RawSQLOperation;
import br.com.caelum.brutal.migration.SchemaMigration;

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
