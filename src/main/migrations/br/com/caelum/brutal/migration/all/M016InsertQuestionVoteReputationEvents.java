package br.com.caelum.brutal.migration.all;

import static java.util.Arrays.asList;

import java.util.List;

import net.vidageek.mirror.dsl.Mirror;

import org.hibernate.Session;
import org.hibernate.StatelessSession;

import br.com.caelum.brutal.migration.Migration;
import br.com.caelum.brutal.migration.MigrationOperation;
import br.com.caelum.brutal.model.EventType;
import br.com.caelum.brutal.model.Question;
import br.com.caelum.brutal.model.ReputationEvent;
import br.com.caelum.brutal.model.Vote;
import br.com.caelum.vraptor.ioc.ApplicationScoped;
import br.com.caelum.vraptor.ioc.Component;

@Component
@ApplicationScoped
public class M016InsertQuestionVoteReputationEvents implements Migration {

	@Override
	public List<MigrationOperation> up() {
		MigrationOperation operation = new MigrationOperation() {
			
			@Override
			@SuppressWarnings("unchecked")
			public void execute(Session session, StatelessSession statelessSession) {
				List<Question> questions = session.createQuery("select a from Question a join fetch a.votes").list();
				for (Question question : questions) {
					List<Vote> votes = (List<Vote>) new Mirror().on(question).get().field("votes");
					for (Vote vote : votes) {
						EventType type = vote.isDown() ? EventType.QUESTION_DOWNVOTE : EventType.QUESTION_UPVOTE;
						ReputationEvent reputationEvent = new ReputationEvent(type, question, question.getAuthor());
						new Mirror().on(reputationEvent).set().field("date").withValue(vote.getCreatedAt());
						statelessSession.insert(reputationEvent);
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
