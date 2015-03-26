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
		String createTable = "create table Attachment (\n" +
				"        id bigint not null auto_increment,\n" +
				"        path varchar(255),\n" +
				"        question_id bigint,\n" +
				"        primary key (id)\n" +
				"    ) ENGINE=InnoDB";
		String createFK = "alter table Attachment \n" +
				"        add index FK_qvvi93082jthrtmhbly12kess (question_id), \n" +
				"        add constraint FK_qvvi93082jthrtmhbly12kess \n" +
				"        foreign key (question_id) \n" +
				"        references Question (id)";

		return RawSQLOperation.forSqls(createTable, createFK);
	}

	@Override
	public List<MigrationOperation> down() {
		return RawSQLOperation.forSqls();
	}
}
