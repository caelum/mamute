package br.com.caelum.brutal.migration.all;

import java.util.List;

import br.com.caelum.brutal.migration.Migration;
import br.com.caelum.brutal.migration.MigrationOperation;
import br.com.caelum.brutal.migration.RawSQLOperation;
import br.com.caelum.vraptor.ioc.ApplicationScoped;
import br.com.caelum.vraptor.ioc.Component;

@Component
@ApplicationScoped
public class M029CreateTableNewsVotes implements Migration {

	@Override
	public List<MigrationOperation> up() {
		String createNewsVotes =   "create table News_Votes (" + 
				"        News_id bigint not null," + 
				"        votes_id bigint not null," + 
				"        unique (votes_id)" + 
				"    ) ENGINE=InnoDB";
		
		String foreignNews = "alter table News_Votes " + 
				"        add index FKA1853A5DFB27AB8D (News_id), " + 
				"        add constraint FKA1853A5DFB27AB8D " + 
				"        foreign key (News_id) " + 
				"        references News (id)";
		
		String foreignVotes = "alter table News_Votes " + 
				"        add index FKA1853A5D2A7767EE (votes_id), " + 
				"        add constraint FKA1853A5D2A7767EE " + 
				"        foreign key (votes_id) " + 
				"        references Vote (id)";
		return RawSQLOperation.forSqls(createNewsVotes, foreignNews, foreignVotes);
	}

	@Override
	public List<MigrationOperation> down() {
		String dropSql = "drop table News_Votes";
		return RawSQLOperation.forSqls(dropSql);
	}

}
