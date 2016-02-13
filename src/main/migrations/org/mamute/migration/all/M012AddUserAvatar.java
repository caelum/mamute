package org.mamute.migration.all;

import org.mamute.migration.MigrationOperation;
import org.mamute.migration.RawSQLOperation;
import org.mamute.migration.SchemaMigration;

import javax.enterprise.context.ApplicationScoped;
import java.util.List;

@ApplicationScoped
public class M012AddUserAvatar implements SchemaMigration {
	@Override
	public List<MigrationOperation> up() {
		String q1 = "alter table Users " +
				"add column avatarImage_id bigint";
		
		String q2 = "alter table Users " +
				"add index FK_gvmhe2prumyg00npgdawfu7la (avatarImage_id), " +
				"add constraint FK_gvmhe2prumyg00npgdawfu7la " +
				"foreign key (avatarImage_id) " +
				"references Attachment (id)";

		return RawSQLOperation.forSqls(q1, q2);
	}

	@Override
	public List<MigrationOperation> down() {
		return RawSQLOperation.forSqls();
	}
}
