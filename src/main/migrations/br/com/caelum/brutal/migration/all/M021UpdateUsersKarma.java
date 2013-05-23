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
public class M021UpdateUsersKarma implements Migration {

	@Override
	public List<MigrationOperation> up() {
		MigrationOperation operation = new MigrationOperation() {
			@Override
			@SuppressWarnings("unchecked")
			public void execute(Session session, StatelessSession statelessSession) {
				String hql = "select u,sum(e.karmaReward) from ReputationEvent e join e.user u group by u order by sum(e.karmaReward)";
				List<Object[]> results = session.createQuery(hql).list();
				for (Object[] objects : results) {
					String update = "update User u set u.karma=:karma where u=:user";
					session.createQuery(update)
						.setParameter("karma", objects[1])
						.setParameter("user", objects[0])
						.executeUpdate();
				}
			}
		};
		return asList(operation);
	}

	@Override
	public List<MigrationOperation> down() {
		return RawSQLOperation.forSqls("");
	}

}