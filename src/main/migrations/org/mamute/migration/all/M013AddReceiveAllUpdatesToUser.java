package org.mamute.migration.all;

import org.mamute.migration.MigrationOperation;
import org.mamute.migration.RawSQLOperation;
import org.mamute.migration.SchemaMigration;

import javax.enterprise.context.ApplicationScoped;
import java.util.List;

@ApplicationScoped
public class M013AddReceiveAllUpdatesToUser implements SchemaMigration {

	@Override
	public List<MigrationOperation> up() {
		String q1 = "alter table Users add column receiveAllUpdates tinyint(1) default false";
		String q2 = "update Users set receiveAllUpdates=false";

		return RawSQLOperation.forSqls(q1, q2);
	}

	@Override
	public List<MigrationOperation> down() {
		return RawSQLOperation.forSqls();
	}
}
