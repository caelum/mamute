package br.com.caelum.brutal.migration.all;

import java.util.List;

import br.com.caelum.brutal.migration.Migration;
import br.com.caelum.brutal.migration.MigrationOperation;
import br.com.caelum.brutal.migration.RawSQLOperation;
import br.com.caelum.vraptor.ioc.ApplicationScoped;
import br.com.caelum.vraptor.ioc.Component;

@Component
@ApplicationScoped
public class M026CreateTableNewsInformation implements Migration {

	@Override
	public List<MigrationOperation> up() {
		String createNewsInformation =   "create table NewsInformation (" + 
				"        id bigint not null auto_increment," + 
				"        comment longtext not null," + 
				"        createdAt datetime," + 
				"        description longtext not null," + 
				"        ip varchar(255)," + 
				"        markedDescription longtext," + 
				"        moderatedAt datetime," + 
				"        sluggedTitle longtext not null," + 
				"        status varchar(255)," + 
				"        title longtext not null," + 
				"        author_id bigint not null," + 
				"        moderatedBy_id bigint," + 
				"        news_id bigint," + 
				"        primary key (id)" + 
				"    ) ENGINE=InnoDB";
		

		return RawSQLOperation.forSqls(createNewsInformation);
	}

	@Override
	public List<MigrationOperation> down() {
		String dropSql = "drop table NewsInformation";
		return RawSQLOperation.forSqls(dropSql);
	}

}
