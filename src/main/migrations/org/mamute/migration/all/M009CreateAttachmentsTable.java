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
			String q1 = "create table Attachment (" +
				" id bigint not null auto_increment," +
				" createdAt datetime," +
				" ip varchar(255)," +
				" mime varchar(255)," +
				" name varchar(255)," +
				" owner_id bigint," +
				" primary key (id)" +
				" ) ENGINE=InnoDB";

		String q2 = "create table Question_Attachment (" +
				" Question_id bigint not null," +
				" attachments_id bigint not null" +
				" ) ENGINE=InnoDB";
		

		String q3 = "alter table Attachment" +
				" add index FK_enrbut32jkvqv2ttop49nkcb4 (owner_id)," +
				" add constraint FK_enrbut32jkvqv2ttop49nkcb4" +
				" foreign key (owner_id)" +
				" references Users (id)";

		String q5 = "alter table Question_Attachment" +
				" add constraint UK_7y9vgsl3nbmms94toj87g69lu unique (attachments_id)";

		String q6 = "alter table Question_Attachment" +
				" add index FK_7y9vgsl3nbmms94toj87g69lu (attachments_id)," +
				" add constraint FK_7y9vgsl3nbmms94toj87g69lu" +
				" foreign key (attachments_id)" +
				" references Attachment (id)";

		String q7 = "alter table Question_Attachment" +
				" add index FK_ib0aqkj0a4a4l9ku9oai3lw9w (Question_id)," +
				" add constraint FK_ib0aqkj0a4a4l9ku9oai3lw9w" +
				" foreign key (Question_id)" +
				" references Question (id)";
		

		return RawSQLOperation.forSqls(q1, q2, q3, q5, q6, q7);
	}

	@Override
	public List<MigrationOperation> down() {
		return RawSQLOperation.forSqls();
	}
}
