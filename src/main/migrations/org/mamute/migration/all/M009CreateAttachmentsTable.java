package org.mamute.migration.all;

import org.mamute.migration.MigrationOperation;
import org.mamute.migration.RawSQLOperation;
import org.mamute.migration.SchemaMigration;

import javax.enterprise.context.ApplicationScoped;
import java.util.List;

@ApplicationScoped
public class M009CreateAttachmentsTable implements SchemaMigration {
	@Override
	public List<MigrationOperation> up() {
		String createTable = "create table Attachment (" +
				"id bigint not null auto_increment," +
				"createdAt datetime," +
				"ip varchar(255)," +
				"path varchar(255)," +
				"mime varchar(255)," +
				"owner_id bigint," +
				"question_id bigint," +
				"primary key (id)" +
				"    ) ENGINE=InnoDB";
		

		String createUsersFK = "alter table Attachment " +
				"add index FK_enrbut32jkvqv2ttop49nkcb4 (owner_id), " +
				"add constraint FK_enrbut32jkvqv2ttop49nkcb4 " +
				"foreign key (owner_id) " +
				"references Users (id) ";

		String createQuestionFK = "alter table Attachment " +
				"add index FK_qvvi93082jthrtmhbly12kess (question_id), " +
				"add constraint FK_qvvi93082jthrtmhbly12kess " +
				"foreign key (question_id) " +
				"references Question (id) ";
		

		return RawSQLOperation.forSqls(createTable, createUsersFK, createQuestionFK);
	}

	@Override
	public List<MigrationOperation> down() {
		return RawSQLOperation.forSqls();
	}
}
