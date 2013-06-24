package br.com.caelum.brutal.migration.all;

import static java.util.Arrays.asList;

import java.util.List;

import net.vidageek.mirror.dsl.Mirror;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.StatelessSession;

import br.com.caelum.brutal.migration.Migration;
import br.com.caelum.brutal.migration.MigrationOperation;
import br.com.caelum.brutal.model.Answer;
import br.com.caelum.brutal.model.EventType;
import br.com.caelum.brutal.model.MassiveVote;
import br.com.caelum.brutal.model.Question;
import br.com.caelum.brutal.model.ReputationEvent;
import br.com.caelum.brutal.model.User;
import br.com.caelum.brutal.model.Vote;
import br.com.caelum.vraptor.ioc.ApplicationScoped;
import br.com.caelum.vraptor.ioc.Component;

@Component
@ApplicationScoped
public class M025InsertMassiveUpvotes implements Migration {

	@Override
	@SuppressWarnings("unchecked")
	public List<MigrationOperation> up() {
		MigrationOperation operation = new MigrationOperation() {
			@Override
			public void execute(Session session, StatelessSession statelessSession) {
				session.createQuery("delete from ReputationEvent where type=:type")
					.setParameter("type", EventType.ANSWER_UPVOTE)
					.executeUpdate();
				
				Query query = session.createQuery("select a,v from Answer a join a.votes v");
				List<Object[]> arrays = query.list();
				
				for (Object[] array : arrays) {
					Answer answer = (Answer) array[0];
					Vote vote = (Vote) array[1];
					
					Long voteCount = (Long) session.createQuery("select count(*) from Answer a join a.votes v where a.author=:answerAuthor and v.author=:voteAuthor and v.createdAt between :start and :end")
						.setParameter("answerAuthor", answer.getAuthor())
						.setParameter("voteAuthor", vote.getAuthor())
						.setParameter("start", vote.getCreatedAt().minusDays(MassiveVote.MIN_DAY))
						.setParameter("end", vote.getCreatedAt())
						.uniqueResult();
					
					ReputationEvent event;
					User author = answer.getAuthor();
					Question question = answer.getQuestion();
					
					if(voteCount > MassiveVote.MAX_VOTE_ALLOWED) {
						event = new ReputationEvent(EventType.MASSIVE_VOTE_IGNORED, question, author);
						author.descreaseKarma(EventType.ANSWER_UPVOTE.reward());
					} else {
						event = new ReputationEvent(EventType.ANSWER_UPVOTE, question, author);
					}
					
					new Mirror().on(event).set().field("date").withValue(vote.getCreatedAt());
					session.save(event);
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
