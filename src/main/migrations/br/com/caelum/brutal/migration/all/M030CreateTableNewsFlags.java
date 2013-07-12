package br.com.caelum.brutal.migration.all;

import java.util.List;

import br.com.caelum.brutal.migration.Migration;
import br.com.caelum.brutal.migration.MigrationOperation;
import br.com.caelum.brutal.migration.RawSQLOperation;
import br.com.caelum.vraptor.ioc.ApplicationScoped;
import br.com.caelum.vraptor.ioc.Component;

@Component
@ApplicationScoped
public class M030CreateTableNewsFlags implements Migration {

	@Override
	public List<MigrationOperation> up() {
		String createNewsFlags =   "create table News_Flags (" + 
				"        News_id bigint not null," + 
				"        flags_id bigint not null," + 
				"        unique (flags_id)" + 
				"    ) ENGINE=InnoDB";
		
		
		String foreignNewsFlagsNews = "alter table News_Flags" + 
				"        add index FKA0A21E1BFB27AB8D (News_id)," + 
				"        add constraint FKA0A21E1BFB27AB8D " + 
				"        foreign key (News_id) " + 
				"        references News (id)";
		
		String foreignNewsFlagsFlags = "alter table News_Flags " + 
				"        add index FKA0A21E1BED5AAFF2 (flags_id), " + 
				"        add constraint FKA0A21E1BED5AAFF2 " + 
				"        foreign key (flags_id) " + 
				"        references Flag (id)";

		
		return RawSQLOperation.forSqls(createNewsFlags, foreignNewsFlagsFlags, foreignNewsFlagsNews);
	}

	@Override
	public List<MigrationOperation> down() {
		String dropSql = "drop table News_Flags";
		return RawSQLOperation.forSqls(dropSql);
	}

}
