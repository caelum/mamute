package br.com.caelum.brutal.migration.all;

import java.util.List;

import br.com.caelum.brutal.migration.Migration;
import br.com.caelum.brutal.migration.MigrationOperation;
import br.com.caelum.brutal.migration.RawSQLOperation;
import br.com.caelum.vraptor.ioc.ApplicationScoped;
import br.com.caelum.vraptor.ioc.Component;

@Component
@ApplicationScoped
public class M012CreateTableWatch implements Migration {

	@Override
	public List<MigrationOperation> up() {
		String createSql = "create table Watcher (id bigint not null auto_increment, active bit not null, watchedQuestion_id bigint, watcher_id bigint, primary key (id)) ENGINE=InnoDB";
		String createUserForeignKey = "alter table Watcher add index FK4F7D4AF8780E8FC (watcher_id), add constraint FK4F7D4AF8780E8FC foreign key (watcher_id) references Users (id)";
		String createQuestionForeignKey = "alter table Watcher add index FK4F7D4AF9A1A0ABF (watchedQuestion_id), add constraint FK4F7D4AF9A1A0ABF foreign key (watchedQuestion_id) references Question (id)"; 
		List<MigrationOperation> ops = RawSQLOperation.forSqls(createSql, createUserForeignKey, createQuestionForeignKey);
		return ops;
	}

	@Override
	public List<MigrationOperation> down() {
		String dropSql = "drop table Watcher";
		return RawSQLOperation.forSqls(dropSql);
	}

}
