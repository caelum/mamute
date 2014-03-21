package org.mamute.migration.all;

import java.util.List;

import javax.enterprise.context.ApplicationScoped;

import org.mamute.migration.MigrationOperation;
import org.mamute.migration.RawSQLOperation;
import org.mamute.migration.SchemaMigration;

@ApplicationScoped
public class M006CreateTableQuestionInteraction implements SchemaMigration {
	@Override
	public List<MigrationOperation> up() {
		String createTable =  "create table Question_Interactions (Question_id bigint not null, userInteractions_id bigint not null, PRIMARY KEY (`Question_id`,`userInteractions_id`)) ENGINE=InnoDB";
		String alterTableFkUser = "alter table Question_Interactions add index FK_nydo4x8ey7gnhhwg1gqmikwo6 (userInteractions_id), add constraint FK_nydo4x8ey7gnhhwg1gqmikwo6 foreign key (userInteractions_id) references Users (id)"; 
		String alterTableFkQuestion = "alter table Question_Interactions add index FK_plnjd89r1mncrtf1vfj65pspt (Question_id), add constraint FK_plnjd89r1mncrtf1vfj65pspt foreign key (Question_id) references Question (id)";  
		
		return RawSQLOperation.forSqls(createTable, alterTableFkUser, alterTableFkQuestion);
	}

	@Override
	public List<MigrationOperation> down() {
		return RawSQLOperation.forSqls("drop table Question_Interactions");
	}
}
