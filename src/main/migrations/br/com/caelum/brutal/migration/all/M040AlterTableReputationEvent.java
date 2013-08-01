package br.com.caelum.brutal.migration.all;

import static java.util.Arrays.asList;

import java.util.List;

import org.hibernate.Session;
import org.hibernate.StatelessSession;

import br.com.caelum.brutal.migration.Migration;
import br.com.caelum.brutal.migration.MigrationOperation;
import br.com.caelum.brutal.migration.RawSQLOperation;
import br.com.caelum.vraptor.ioc.ApplicationScoped;
import br.com.caelum.vraptor.ioc.Component;

@Component
@ApplicationScoped
public class M040AlterTableReputationEvent implements Migration {
	@Override
	public List<MigrationOperation> up() {
		List<MigrationOperation> ops = RawSQLOperation.forSqls("alter table ReputationEvent add column context_type varchar(255);");
		ops.add(new MigrationOperation() {
			@Override
			public void execute(Session session, StatelessSession statelessSession) {
				session.createSQLQuery("update ReputationEvent set context_type = 'QUESTION' where questionInvolved_id is not NULL;").executeUpdate();
				session.createSQLQuery("update ReputationEvent set context_type = 'NEWS' where questionInvolved_id is NULL;").executeUpdate();
				session.createSQLQuery("alter table ReputationEvent drop foreign key FK43AAD98140A4286").executeUpdate();
				session.createSQLQuery("alter table ReputationEvent change questionInvolved_id context_id bigint(20);").executeUpdate();
			}
		});
		return ops;
	}

	@Override
	public List<MigrationOperation> down() {
		return asList();
	}
}
