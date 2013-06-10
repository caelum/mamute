package br.com.caelum.brutal.migration.all;

import static java.util.Arrays.asList;

import java.util.List;

import net.vidageek.mirror.dsl.Mirror;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.StatelessSession;

import br.com.caelum.brutal.migration.Migration;
import br.com.caelum.brutal.migration.MigrationOperation;
import br.com.caelum.brutal.model.User;
import br.com.caelum.vraptor.ioc.ApplicationScoped;
import br.com.caelum.vraptor.ioc.Component;

@Component
@ApplicationScoped
public class M023UpdateNewsletterSubscriptions  implements Migration {

	@Override
	@SuppressWarnings("unchecked")
	public List<MigrationOperation> up() {
		MigrationOperation operation = new MigrationOperation() {
			@Override
			public void execute(Session session, StatelessSession statelessSession) {
				Query query = session.createQuery("select u from UserSession us join us.user u");
				List<User> users = query.list();
				for (User user : users) {
					new Mirror().on(user).set().field("isSubscribed").withValue(true);
				}
			}
		};
		return asList(operation);
	}

	@Override
	public List<MigrationOperation> down() {
		return asList();
	}

}
