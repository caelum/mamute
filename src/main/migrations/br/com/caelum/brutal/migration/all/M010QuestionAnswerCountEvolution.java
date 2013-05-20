package br.com.caelum.brutal.migration.all;

import static java.util.Arrays.asList;

import java.util.List;

import org.hibernate.Session;
import org.hibernate.StatelessSession;

import br.com.caelum.brutal.migration.Migration;
import br.com.caelum.brutal.migration.MigrationOperation;
import br.com.caelum.brutal.model.Answer;
import br.com.caelum.vraptor.ioc.ApplicationScoped;
import br.com.caelum.vraptor.ioc.Component;

@Component
@ApplicationScoped
public class M010QuestionAnswerCountEvolution implements Migration {

	@Override
	@SuppressWarnings("unchecked")
	public List<MigrationOperation> up() {
		MigrationOperation recalculate = new MigrationOperation() {
			@Override
			public void execute(Session session, StatelessSession statelessSession) {
				List<Answer> answers = session.createQuery("from Answer where invisible = true").list();
				for (Answer answer : answers) {
					answer.remove();
				}
			}
		};
		return asList(recalculate);
	}

	@Override
	public List<MigrationOperation> down() {
		return asList();
	}

}
