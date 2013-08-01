package br.com.caelum.brutal.migration.all;

import static java.util.Arrays.asList;

import java.util.List;

import net.vidageek.mirror.dsl.Mirror;

import org.hibernate.Session;
import org.hibernate.StatelessSession;

import br.com.caelum.brutal.migration.Migration;
import br.com.caelum.brutal.migration.MigrationOperation;
import br.com.caelum.brutal.model.Comment;
import br.com.caelum.brutal.model.EventType;
import br.com.caelum.brutal.model.News;
import br.com.caelum.brutal.model.NewsCommentList;
import br.com.caelum.brutal.model.ReputationEvent;
import br.com.caelum.brutal.model.Vote;
import br.com.caelum.vraptor.ioc.ApplicationScoped;
import br.com.caelum.vraptor.ioc.Component;

@Component
@ApplicationScoped
public class M042InsertNewsCommentsVotesReputationEvents implements Migration{
	@Override
	public List<MigrationOperation> up() {
		MigrationOperation ops = new MigrationOperation() {
			@SuppressWarnings("unchecked")
			@Override
			public void execute(Session session, StatelessSession statelessSession) {
				List<News> news = session.createQuery("select n from News n join fetch n.comments").list();
				for (News n : news) {
					NewsCommentList newsCommentsList = (NewsCommentList) new Mirror().on(n).get().field("comments");
					List<Comment> comments = (List<Comment>) new Mirror().on(newsCommentsList).get().field("comments");
					
					for (Comment c : comments) {					
						if (c.getVoteCount() > 0){
							List<Vote> votes = (List<Vote>) new Mirror().on(c).get().field("votes");
							for (Vote v : votes) {
								ReputationEvent reputationEvent = new ReputationEvent(EventType.COMMENT_UPVOTE, n, c.getAuthor());
								new Mirror().on(reputationEvent).set().field("date").withValue(v.getCreatedAt());
								statelessSession.insert(reputationEvent);
							}
						}
					}
				}
			}
		};
		return asList(ops);
	}

	@Override
	public List<MigrationOperation> down() {
		// TODO Auto-generated method stub
		return asList();
	}
}
