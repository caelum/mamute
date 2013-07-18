package br.com.caelum.brutal.migration.all;

import static br.com.caelum.brutal.migration.RawSQLOperation.forSqls;

import java.util.List;

import br.com.caelum.brutal.migration.Migration;
import br.com.caelum.brutal.migration.MigrationOperation;
import br.com.caelum.vraptor.ioc.ApplicationScoped;
import br.com.caelum.vraptor.ioc.Component;

@Component
@ApplicationScoped
public class M034CreateTableQuestionWatchersJoinTable implements Migration{

	@Override
	public List<MigrationOperation> up() {
		String create = "create table Question_Watchers ("+
			        "Question_id bigint not null,"+
			        "watchers_id bigint not null,"+
			        "unique (watchers_id)"+
		        ") ENGINE=InnoDB";
		
		String foreignToWatchers = "alter table Question_Watchers "+ 
		        "add index FK734DFA704E3F766D (watchers_id),"+
		        "add constraint FK734DFA704E3F766D "+
		        "foreign key (watchers_id) "+
		        "references Watcher (id)";

		String foreignToQuestion = "alter table Question_Watchers "+ 
				"add index FK734DFA70D6CA1D2D (Question_id)," + 
				"add constraint FK734DFA70D6CA1D2D " + 
				"foreign key (Question_id) " + 
				"references Question (id)"; 
		
		return forSqls(create, foreignToWatchers, foreignToQuestion);
	}

	@Override
	public List<MigrationOperation> down() {
		// TODO Auto-generated method stub
		return null;
	}

}
