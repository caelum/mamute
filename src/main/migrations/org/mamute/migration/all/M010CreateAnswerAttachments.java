package org.mamute.migration.all;

import org.mamute.migration.MigrationOperation;
import org.mamute.migration.RawSQLOperation;
import org.mamute.migration.SchemaMigration;

import javax.enterprise.context.ApplicationScoped;
import java.util.List;

@ApplicationScoped
public class M010CreateAnswerAttachments implements SchemaMigration {
	@Override
	public List<MigrationOperation> up() {
		String q1= "create table Answer_Attachment (\n" +
				" Answer_id bigint not null,\n" +
				" attachments_id bigint not null,\n" +
				" primary key (Answer_id, attachments_id)\n" +
				" ) ENGINE=InnoDB";

		String q2 = "alter table Answer_Attachment\n" +
				" add constraint UK_m8lisjgd2lw0uy50bxngkvi5o unique (attachments_id)";

		String q3 = "alter table Answer_Attachment\n" +
				" add index FK_m8lisjgd2lw0uy50bxngkvi5o (attachments_id),\n" +
				" add constraint FK_m8lisjgd2lw0uy50bxngkvi5o\n" +
				" foreign key (attachments_id)\n" +
				" references Attachment (id)";

		String q4 = "alter table Answer_Attachment\n" +
				" add index FK_2r3h5i8jc5w2kqqhnhhlvenht (Answer_id),\n" +
				" add constraint FK_2r3h5i8jc5w2kqqhnhhlvenht\n" +
				" foreign key (Answer_id)\n" +
				" references Answer (id)";

		return RawSQLOperation.forSqls(q1, q2, q3, q4);
	}

	@Override
	public List<MigrationOperation> down() {
		return RawSQLOperation.forSqls();
	}
}
