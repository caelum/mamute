package br.com.caelum.brutal.migration.all;

import static br.com.caelum.brutal.migration.RawSQLOperation.forSqls;
import static java.util.Arrays.asList;

import java.util.List;

import org.hibernate.Session;
import org.hibernate.StatelessSession;

import br.com.caelum.brutal.migration.Migration;
import br.com.caelum.brutal.migration.MigrationOperation;
import br.com.caelum.vraptor.ioc.ApplicationScoped;
import br.com.caelum.vraptor.ioc.Component;

@Component
@ApplicationScoped
public class M011AddColumnCreatedAtIntoUserSession implements Migration {

	@Override
	public List<MigrationOperation> up() {
		List<MigrationOperation> ops = forSqls("alter table UserSession add column createdAt datetime");
		ops.add(new MigrationOperation() {
			@Override
			public void execute(Session session, StatelessSession statelessSession) {
				session.createSQLQuery("update UserSession set createdAt = now();").executeUpdate();
			}
		});
		return ops;
	}

	@Override
	public List<MigrationOperation> down() {
		return asList();
	}

}
