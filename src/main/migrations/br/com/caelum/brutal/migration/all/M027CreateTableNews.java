package br.com.caelum.brutal.migration.all;

import java.util.List;

import br.com.caelum.brutal.migration.Migration;
import br.com.caelum.brutal.migration.MigrationOperation;
import br.com.caelum.brutal.migration.RawSQLOperation;
import br.com.caelum.vraptor.ioc.ApplicationScoped;
import br.com.caelum.vraptor.ioc.Component;

@Component
@ApplicationScoped
public class M027CreateTableNews implements Migration {

	@Override
	public List<MigrationOperation> up() {
		String createNews = "create table News (" + 
				"        id bigint not null auto_increment," + 
				"        createdAt datetime," + 
				"        lastUpdatedAt datetime," + 
				"        invisible bit not null," + 
				"        views bigint not null," + 
				"        voteCount bigint not null," + 
				"        author_id bigint," + 
				"        information_id bigint not null," + 
				"        lastTouchedBy_id bigint," + 
				"        primary key (id)" + 
				"    ) ENGINE=InnoDB";
		
		return RawSQLOperation.forSqls(createNews);
	}

	@Override
	public List<MigrationOperation> down() {
		String dropSql = "drop table News";
		return RawSQLOperation.forSqls(dropSql);
	}

}
