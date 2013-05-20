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
import br.com.caelum.brutal.model.ReputationEvent;
import br.com.caelum.brutal.model.Vote;
import br.com.caelum.vraptor.ioc.ApplicationScoped;
import br.com.caelum.vraptor.ioc.Component;

@Component
@ApplicationScoped
public class M015InsertAnswerVoteReputationEvents implements Migration {

	@Override
	public List<MigrationOperation> up() {
		MigrationOperation operation = new MigrationOperation() {
			
			@Override
			@SuppressWarnings("unchecked")
			public void execute(Session session, StatelessSession statelessSession) {
				List<Answer> answers = session.createQuery("select a from Answer a join fetch a.votes").list();
				for (Answer answer : answers) {
					List<Vote> votes = (List<Vote>) new Mirror().on(answer).get().field("votes");
					for (Vote vote : votes) {
						EventType type = vote.isDown() ? EventType.ANSWER_DOWNVOTE : EventType.ANSWER_UPVOTE;
						statelessSession.insert(new ReputationEvent(type, answer.getQuestion(), answer.getAuthor()));
					}
				}
			}
		};
		return asList(operation);
	}

	@Override
	public List<MigrationOperation> down() {
		return RawSQLOperation.forSqls("drop table ReputationEvent");
	}

}
