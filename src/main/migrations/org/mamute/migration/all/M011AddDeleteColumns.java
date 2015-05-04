package org.mamute.migration.all;

import org.mamute.migration.MigrationOperation;
import org.mamute.migration.RawSQLOperation;
import org.mamute.migration.SchemaMigration;

import javax.enterprise.context.ApplicationScoped;
import java.util.List;

@ApplicationScoped
public class M011AddDeleteColumns implements SchemaMigration {
	@Override
	public List<MigrationOperation> up() {
		String q1 = "alter table Question add column deleted tinyint(1) default false";
		String q2 = "update Question set deleted=false";

		String q3 = "alter table Answer add column deleted tinyint(1) default false";
		String q4 = "update Answer set deleted=false";

		String q5 = "alter table Comment add column deleted tinyint(1) default false";
		String q6 = "update Comment set deleted=false";

		String q7 = "alter table Flag add column deleted tinyint(1) default false";
		String q8 = "update Flag set deleted=false";

		String q9 = "alter table ReputationEvent add column deleted tinyint(1) default false";
		String q10 = "update ReputationEvent set deleted=false";

		return RawSQLOperation.forSqls(q1, q2, q3, q4, q5, q6, q7, q8, q9, q10);
	}

	@Override
	public List<MigrationOperation> down() {
		return RawSQLOperation.forSqls();
	}
}
