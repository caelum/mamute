package br.com.caelum.brutal.migration.all;

import static java.util.Arrays.asList;

import java.util.List;

import net.vidageek.mirror.dsl.Mirror;

import org.hibernate.Session;
import org.hibernate.StatelessSession;

import br.com.caelum.brutal.migration.Migration;
import br.com.caelum.brutal.migration.MigrationOperation;
import br.com.caelum.brutal.migration.RawSQLOperation;
import br.com.caelum.brutal.model.Answer;
import br.com.caelum.brutal.model.EventType;
import br.com.caelum.brutal.model.Question;
import br.com.caelum.brutal.model.ReputationEvent;
import br.com.caelum.vraptor.ioc.ApplicationScoped;
import br.com.caelum.vraptor.ioc.Component;

@Component
@ApplicationScoped
public class M018InsertCreatedQuestionAndAnswerReputationEvents implements Migration {

	@Override
	public List<MigrationOperation> up() {
		MigrationOperation operation = new MigrationOperation() {
			@Override
			@SuppressWarnings("unchecked")
			public void execute(Session session, StatelessSession statelessSession) {
				List<Question> questions = session.createCriteria(Question.class).list();
				for (Question question : questions) {
					ReputationEvent reputationEvent = new ReputationEvent(EventType.CREATED_QUESTION, question, question.getAuthor());
					new Mirror().on(reputationEvent).set().field("date").withValue(question.getCreatedAt());
					statelessSession.insert(reputationEvent);
				}
				List<Answer> answers = session.createCriteria(Answer.class).list();
				for (Answer answer : answers) {
					ReputationEvent reputationEvent = new ReputationEvent(EventType.CREATED_ANSWER, answer.getMainThread(), answer.getAuthor());
					new Mirror().on(reputationEvent).set().field("date").withValue(answer.getCreatedAt());
					statelessSession.insert(reputationEvent);
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
