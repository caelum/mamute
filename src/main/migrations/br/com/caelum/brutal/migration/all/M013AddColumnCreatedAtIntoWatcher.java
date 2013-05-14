package br.com.caelum.brutal.migration.all;

import static java.util.Arrays.asList;

import java.util.List;

import org.hibernate.Session;

import br.com.caelum.brutal.migration.Migration;
import br.com.caelum.brutal.migration.MigrationOperation;
import br.com.caelum.brutal.migration.RawSQLOperation;
import br.com.caelum.vraptor.ioc.ApplicationScoped;
import br.com.caelum.vraptor.ioc.Component;

@Component
@ApplicationScoped
public class M013AddColumnCreatedAtIntoWatcher implements Migration{

	@Override
	public List<MigrationOperation> up() {
		List<MigrationOperation> operations = RawSQLOperation.forSqls("alter table Watcher add column createdAt datetime");
		operations.add(new MigrationOperation() {
			@Override
			public void execute(Session session) {
				session.createSQLQuery("update Watcher set createdAt = now();").executeUpdate();
			}
		});
		return operations;
	}

	@Override
	public List<MigrationOperation> down() {
		return asList();
	}

}
