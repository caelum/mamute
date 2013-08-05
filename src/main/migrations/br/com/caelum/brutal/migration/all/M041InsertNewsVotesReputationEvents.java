package br.com.caelum.brutal.migration.all;

import static java.util.Arrays.asList;

import java.util.List;

import net.vidageek.mirror.dsl.Mirror;

import org.hibernate.Session;
import org.hibernate.StatelessSession;

import br.com.caelum.brutal.migration.Migration;
import br.com.caelum.brutal.migration.MigrationOperation;
import br.com.caelum.brutal.model.EventType;
import br.com.caelum.brutal.model.News;
import br.com.caelum.brutal.model.ReputationEvent;
import br.com.caelum.brutal.model.Vote;
import br.com.caelum.vraptor.ioc.ApplicationScoped;
import br.com.caelum.vraptor.ioc.Component;

@Component
@ApplicationScoped
public class M041InsertNewsVotesReputationEvents implements Migration{
	@Override
	public List<MigrationOperation> up() {
		MigrationOperation ops = new MigrationOperation() {
			@SuppressWarnings("unchecked")
			@Override
			public void execute(Session session, StatelessSession statelessSession) {
				List<News> news = session.createQuery("select distinct n from News n join fetch n.votes").list();
				for (News n : news) {
					List<Vote> votes = (List<Vote>) new Mirror().on(n).get().field("votes");
					for (Vote vote : votes) {
						if (vote.isDown()){
							EventType typeForVoteAuthor = EventType.DOWNVOTED_SOMETHING;
							EventType typeForNewsAuthor = EventType.NEWS_DOWNVOTE;
							ReputationEvent reForVoteAuthor = new ReputationEvent(typeForVoteAuthor, n, vote.getAuthor());
							ReputationEvent reForNewsAuthor = new ReputationEvent(typeForNewsAuthor, n, n.getAuthor());
							statelessSession.insert(reForNewsAuthor);
							statelessSession.insert(reForVoteAuthor);
						}else{
							EventType type = EventType.NEWS_UPVOTE;
							ReputationEvent reputationEvent = new ReputationEvent(type, n, n.getAuthor());
							new Mirror().on(reputationEvent).set().field("date").withValue(vote.getCreatedAt());
							statelessSession.insert(reputationEvent);
						}
					}
				}

			}
		};
		return asList(ops);
	}

	@Override
	public List<MigrationOperation> down() {
		return asList();
	}
}
