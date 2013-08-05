package br.com.caelum.brutal.migration.all;

import static java.util.Arrays.asList;

import java.util.List;

import net.vidageek.mirror.dsl.Mirror;

import org.hibernate.Session;
import org.hibernate.StatelessSession;
import org.joda.time.DateTime;

import br.com.caelum.brutal.dao.VoteDAO;
import br.com.caelum.brutal.migration.Migration;
import br.com.caelum.brutal.migration.MigrationOperation;
import br.com.caelum.brutal.migration.RawSQLOperation;
import br.com.caelum.brutal.model.Comment;
import br.com.caelum.brutal.model.EventType;
import br.com.caelum.brutal.model.ReputationEvent;
import br.com.caelum.brutal.model.ReputationEventContext;
import br.com.caelum.brutal.model.Vote;
import br.com.caelum.vraptor.ioc.ApplicationScoped;
import br.com.caelum.vraptor.ioc.Component;

@Component
@ApplicationScoped
public class M019CommentUpvoteReputationEvents implements Migration {

	@Override
	public List<MigrationOperation> up() {
		MigrationOperation operation = new MigrationOperation() {
			@Override
			@SuppressWarnings("unchecked")
			public void execute(Session session, StatelessSession statelessSession) {
				List<Comment> comments = session.createCriteria(Comment.class).list();
				VoteDAO voteDAO = new VoteDAO(session);
				for (Comment comment : comments) {
					ReputationEventContext question = voteDAO.contextOf(comment);
					List<Vote> votes = comment.getVotes();
					for (Vote vote : votes) {
						ReputationEvent reputationEvent = new ReputationEvent(EventType.COMMENT_UPVOTE, question, comment.getAuthor());
						DateTime createdAt = vote.getCreatedAt();
						new Mirror().on(reputationEvent).set().field("date").withValue(createdAt);
						statelessSession.insert(reputationEvent);
					}
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
