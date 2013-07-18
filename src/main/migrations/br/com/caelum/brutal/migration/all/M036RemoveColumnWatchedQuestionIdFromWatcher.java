package br.com.caelum.brutal.migration.all;

import static br.com.caelum.brutal.migration.RawSQLOperation.forSqls;
import static java.util.Arrays.asList;

import java.util.List;

import br.com.caelum.brutal.migration.Migration;
import br.com.caelum.brutal.migration.MigrationOperation;
import br.com.caelum.vraptor.ioc.ApplicationScoped;
import br.com.caelum.vraptor.ioc.Component;

@Component
@ApplicationScoped
public class M036RemoveColumnWatchedQuestionIdFromWatcher implements Migration {

	@Override
	public List<MigrationOperation> up() {
		String removeConstraint = "alter table Watcher drop foreign key FK4F7D4AF9A1A0ABF;"; 
		String removeColumn = "alter table Watcher drop watchedQuestion_id;"; 
		return forSqls(removeConstraint, removeColumn);
	}

	@Override
	public List<MigrationOperation> down() {
		return asList();
	}

}
