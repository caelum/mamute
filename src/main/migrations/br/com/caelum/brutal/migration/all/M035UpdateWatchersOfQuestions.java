package br.com.caelum.brutal.migration.all;

import static java.util.Arrays.asList;

import java.math.BigInteger;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.StatelessSession;

import br.com.caelum.brutal.migration.Migration;
import br.com.caelum.brutal.migration.MigrationOperation;
import br.com.caelum.brutal.model.Question;
import br.com.caelum.brutal.model.watch.Watcher;
import br.com.caelum.vraptor.ioc.ApplicationScoped;
import br.com.caelum.vraptor.ioc.Component;

@Component
@ApplicationScoped
public class M035UpdateWatchersOfQuestions  implements Migration {

	@Override
	@SuppressWarnings("unchecked")
	public List<MigrationOperation> up() {
		MigrationOperation operation = new MigrationOperation() {
			@Override
			public void execute(Session session, StatelessSession statelessSession) {
				Query query = session.createQuery("from Watcher");
				List<Watcher> watchers = query.list();
				for (Watcher watcher : watchers) {
					BigInteger idOfQuestion = (BigInteger) session.createSQLQuery("select watchedQuestion_id from Watcher where id = "+watcher.getId()).uniqueResult();
					if(idOfQuestion != null){
						Question question = (Question) session.createQuery("from Question where id = :id")
								.setParameter("id", idOfQuestion.longValue())
								.uniqueResult();
						question.add(watcher);
					}else{
						session.delete(watcher);
					}
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
