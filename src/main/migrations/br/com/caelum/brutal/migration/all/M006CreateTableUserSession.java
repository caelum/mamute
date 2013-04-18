package br.com.caelum.brutal.migration.all;

import java.util.List;

import br.com.caelum.brutal.migration.Migration;
import br.com.caelum.brutal.migration.MigrationOperation;
import br.com.caelum.brutal.migration.RawSQLOperation;
import br.com.caelum.vraptor.ioc.ApplicationScoped;
import br.com.caelum.vraptor.ioc.Component;

@Component
@ApplicationScoped
public class M006CreateTableUserSession implements Migration {

	public List<MigrationOperation> up() {
		String createSql = "create table UserSession (id bigint not null auto_increment, sessionKey varchar(255) unique, user_id bigint, primary key (id)) ENGINE=InnoDB";
		String createForeignKey = "alter table UserSession add index FKC7BC0C2B845C688D (user_id), add constraint FKC7BC0C2B845C688D foreign key (user_id) references Users (id)";
		String createIndex = "create index session_key on UserSession (sessionKey)";
		List<MigrationOperation> ops = RawSQLOperation.forSqls(createSql, createForeignKey, createIndex);
		return ops;
	}

	@Override
	public List<MigrationOperation> down() {
		String dropSql = "drop table UserSession";
		return RawSQLOperation.forSqls(dropSql);
	}

}
