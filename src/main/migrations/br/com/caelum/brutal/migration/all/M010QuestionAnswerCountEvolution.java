package br.com.caelum.brutal.migration.all;

import static java.util.Arrays.asList;

import java.util.List;

import org.hibernate.Session;

import br.com.caelum.brutal.migration.Migration;
import br.com.caelum.brutal.migration.MigrationOperation;
import br.com.caelum.brutal.model.Answer;
import br.com.caelum.vraptor.ioc.ApplicationScoped;
import br.com.caelum.vraptor.ioc.Component;

@Component
@ApplicationScoped
public class M010QuestionAnswerCountEvolution implements Migration {

	@Override
	public List<MigrationOperation> up() {
		MigrationOperation insertTags = new MigrationOperation() {
			@Override
			public void execute(Session session) {
				List<Answer> answers = session.createQuery("from Answer where invisible = true").list();
				for (Answer answer : answers) {
					answer.remove();
				}
			}
		};
		return asList(insertTags);
	}

	@Override
	public List<MigrationOperation> down() {
		return asList();
	}

}
