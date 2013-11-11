package br.com.caelum.brutal.migration.all;

import java.util.List;

import javax.enterprise.context.ApplicationScoped;

import br.com.caelum.brutal.migration.MigrationOperation;
import br.com.caelum.brutal.migration.RawSQLOperation;
import br.com.caelum.brutal.migration.SchemaMigration;

@ApplicationScoped
public class M006CreateTableQuestionInteraction implements SchemaMigration {
	@Override
	public List<MigrationOperation> up() {
		String createTable =  "create table Question_Interactions (Question_id bigint not null, userInteractions_id bigint not null ) ENGINE=InnoDB";
		String alterTableFkUser = "alter table Question_Interactions add index FK_hrujl8eih9e72ve2xoddb36mm (userInteractions_id), add constraint FK_hrujl8eih9e72ve2xoddb36mm foreign key (userInteractions_id) references Users (id)"; 
		String alterTableFkQuestion = "alter table Question_Interactions add index FK_chwtbfil7uh215g0yem6997k3 (Question_id), add constraint FK_chwtbfil7uh215g0yem6997k3 foreign key (Question_id) references Question (id)";  
		
		return RawSQLOperation.forSqls(createTable, alterTableFkUser, alterTableFkQuestion);
	}

	@Override
	public List<MigrationOperation> down() {
		return RawSQLOperation.forSqls("drop table Question_Interactions");
	}
}
