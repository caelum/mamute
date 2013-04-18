package br.com.caelum.brutal.migration.all;

import java.math.BigInteger;
import java.util.Arrays;
import java.util.List;

import org.hibernate.SQLQuery;
import org.hibernate.Session;

import br.com.caelum.brutal.migration.Migration;
import br.com.caelum.brutal.migration.MigrationOperation;
import br.com.caelum.brutal.model.User;
import br.com.caelum.brutal.model.UserSession;
import br.com.caelum.vraptor.ioc.ApplicationScoped;
import br.com.caelum.vraptor.ioc.Component;

@Component
@ApplicationScoped
public class M007CopySessionKeyToUserSessionTable implements Migration {

	@Override
	public List<MigrationOperation> up() {
		MigrationOperation copySessionKey = new MigrationOperation() {
			@SuppressWarnings("unchecked")
			@Override
			public void execute(Session session) {
				SQLQuery sqlQuery = session.createSQLQuery("select u.id,u.sessionKey from Users u where sessionKey is not null");
				List<Object[]> userIdsAndKeys = sqlQuery.list();
				for (Object[] userIdAndKey : userIdsAndKeys) {
					BigInteger userId = (BigInteger) userIdAndKey[0];
					String sessionKey = (String) userIdAndKey[1];
					User user = (User) session.load(User.class, userId.longValue());
					session.save(new UserSession(user, sessionKey));
				}
			}
		};
		return Arrays.asList(copySessionKey);
	}

	@Override
	public List<MigrationOperation> down() {
		return Arrays.asList();
	}

}
