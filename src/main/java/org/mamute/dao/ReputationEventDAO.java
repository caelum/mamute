package org.mamute.dao;

import static org.mamute.model.EventType.ANSWERER_RELATED_EVENTS;
import static org.mamute.model.EventType.ASKER_RELATED_EVENTS;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.Session;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.mamute.dto.KarmaByContextHistory;
import org.mamute.dto.UserSummaryForTag;
import org.mamute.model.ReputationEvent;
import org.mamute.model.ReputationEventContext;
import org.mamute.model.Tag;
import org.mamute.model.User;

public class ReputationEventDAO {
	private static final int TOP_ANSWERERS = 20;
	private static final DateTimeFormatter DATE_FMT = DateTimeFormat.forPattern("yyyy/MM/dd");

	private Session session;
	private InvisibleForUsersRule invisible;

	@Deprecated
	public ReputationEventDAO() {
	}

	@Inject
	public ReputationEventDAO(Session session, InvisibleForUsersRule invisible) {
		this.session = session;
		this.invisible = invisible;
	}

	public void save(ReputationEvent reputationEvent) {
		if (!reputationEvent.equals(ReputationEvent.IGNORED_EVENT)) {
			session.save(reputationEvent);
		}
	}

	public void delete(ReputationEvent event) {
		Query query = session.createQuery("delete ReputationEvent e where e.type=:type and e.context=:context and e.user=:user");
		query.setParameter("type", event.getType())
			.setParameter("context", event.getContext())
			.setParameter("user", event.getUser())
			.executeUpdate();
	}

	@SuppressWarnings("unchecked")
	public KarmaByContextHistory karmaWonByQuestion(User user, DateTime after, Integer maxResults) {
		return karmaByContext(user, after, maxResults);
	}

	@SuppressWarnings("unchecked")
	public KarmaByContextHistory karmaWonByQuestion(User user, DateTime after) {
		return karmaByContext(user, after, null);
	}

	@SuppressWarnings("unchecked")
	private KarmaByContextHistory karmaByContext(User user, DateTime after, Integer maxResult) {
		String hql = "select e.context.class, e.context.id, sum(e.karmaReward), " +
				    "e.date " +
				    "from ReputationEvent e "+
			        "where e.user = :user and e.date > :after " +
			        "group by e.context.class, e.context.id, e.date " +
			        "order by e.date desc";

		//get aggregate list
	    Query query = session.createQuery(hql).setParameter("user", user).setParameter("after", after);
		if(maxResult != null){
			query.setMaxResults(maxResult);
		}
	    return new KarmaByContextHistory(fetchContextData(query.list()));
	}

	@SuppressWarnings("unchecked")
	private List<Object[]> fetchContextData(List<Object[]> rows){
		//separate id lists by class
		List<Object[]> organizedData = new ArrayList<>();
		for(Object[] row : rows){
			String contextClass = (String) row[0];
			Long contextId = (Long) row[1];
			
			ReputationEventContext context = (ReputationEventContext) session.createQuery("from "+contextClass+" where id = :id").setParameter("id", contextId).uniqueResult();

			// Skip invalid context entries (because the related item can have been deleted).
			if (context == null) {
				continue;
			}

			organizedData.add(new Object[]{
					context,
					row[2],
					row[3]
			});
		}

		return organizedData;
	}


	@SuppressWarnings("unchecked")
	public KarmaByContextHistory karmaWonByQuestion(User user) {
		String hql = "select e.context, sum(e.karmaReward), e.date from ReputationEvent e " +
				"where e.user=:user " +
				"group by e.context, day(e.date) " +
				"order by e.date desc";

		Query query = session.createQuery(hql).setParameter("user", user);
		return new KarmaByContextHistory(query.list());
	}

	@SuppressWarnings("unchecked")
	public List<UserSummaryForTag> getTopAnswerersSummaryAllTime(Tag tag) {
		return getTopAnswerersSummary(tag, null).list();
	}

	@SuppressWarnings("unchecked")
	public List<UserSummaryForTag> getTopAnswerersSummaryAfter(Tag tag, DateTime after) {
		return getTopAnswerersSummary(tag, after).setParameter("after", after).list();
	}

	private Query getTopAnswerersSummary(Tag tag, DateTime after) {
		String where = after == null ? "" : "and e.date > :after ";
		String hql = "select new org.mamute.dto.UserSummaryForTag(sum(e.karmaReward) as karmaSum, count(distinct a), user) "
				+ "from ReputationEvent e, Question q "+
				"join e.user user "+
				"join q.answers a "+
				"join q.information.tags t "+
				"where user=a.author and e.type in (:events)" +
				where +
				"and t=:tag " +
				"and q.id=e.context.id and e.context.class='QUESTION'"+
				"group by user "+
				"order by karmaSum desc";

		return session.createQuery(hql)
				.setParameterList("events", ANSWERER_RELATED_EVENTS())
				.setParameter("tag", tag).setMaxResults(TOP_ANSWERERS);
	}

	@SuppressWarnings("unchecked")
	public List<UserSummaryForTag> getTopAskersSummaryAllTime(Tag tag) {
		return getTopAskersSummary(tag, null).list();
	}

	@SuppressWarnings("unchecked")
	public List<UserSummaryForTag> getTopAskersSummaryAfter(Tag tag, DateTime after) {
		return getTopAskersSummary(tag, after).setParameter("after", after).list();
	}

	private Query getTopAskersSummary(Tag tag, DateTime after) {
		String where = after == null ? "" : "and e.date > :after ";
		String hql = "select new "
				+ "org.mamute.dto.UserSummaryForTag(sum(e.karmaReward) as karmaSum, count(distinct q), user) "
				+ "from ReputationEvent e, Question q "+
				"join e.user user "+
				"join q.information.tags t "+
				"where e.type in (:events)" + where + "and t=:tag and e.context.id = q.id and e.context.class='QUESTION' "+
				"group by user "+
				"order by karmaSum desc";

		return session.createQuery(hql)
				.setParameterList("events", ASKER_RELATED_EVENTS())
				.setParameter("tag", tag)
				.setMaxResults(TOP_ANSWERERS);
	}

	private Criteria addInvisibleFilter(Criteria criteria){
		return invisible.addFilter("q", criteria);
	}

}
