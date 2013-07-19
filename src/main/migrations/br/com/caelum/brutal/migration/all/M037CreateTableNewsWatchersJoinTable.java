package br.com.caelum.brutal.migration.all;

import static br.com.caelum.brutal.migration.RawSQLOperation.forSqls;

import java.util.List;

import br.com.caelum.brutal.migration.Migration;
import br.com.caelum.brutal.migration.MigrationOperation;
import br.com.caelum.vraptor.ioc.ApplicationScoped;
import br.com.caelum.vraptor.ioc.Component;

@Component
@ApplicationScoped
public class M037CreateTableNewsWatchersJoinTable implements Migration{

	@Override
	public List<MigrationOperation> up() {
		String create = "create table News_Watchers (News_id bigint not null, watchers_id bigint not null, unique (watchers_id)) ENGINE=InnoDB"; 
		String foreignToWatchers = "alter table News_Watchers add index FKD20D16234E3F766D (watchers_id), add constraint FKD20D16234E3F766D foreign key (watchers_id) references Watcher (id)";
		String foreignToQuestion = "alter table News_Watchers add index FKD20D1623FB27AB8D (News_id), add constraint FKD20D1623FB27AB8D foreign key (News_id) references News (id)";
		
		return forSqls(create, foreignToWatchers, foreignToQuestion);
	}

	@Override
	public List<MigrationOperation> down() {
		return null;
	}

}
