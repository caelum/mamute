package br.com.caelum.brutal.dao;

import static br.com.caelum.brutal.model.EventType.ANSWERER_RELATED_EVENTS;
import static br.com.caelum.brutal.model.EventType.ASKER_RELATED_EVENTS;
import static org.hibernate.criterion.Order.desc;
import static org.hibernate.criterion.Projections.alias;
import static org.hibernate.criterion.Projections.groupProperty;
import static org.hibernate.criterion.Projections.projectionList;
import static org.hibernate.criterion.Projections.property;
import static org.hibernate.criterion.Projections.sqlGroupProjection;
import static org.hibernate.criterion.Projections.sum;
import static org.hibernate.criterion.Restrictions.and;
import static org.hibernate.criterion.Restrictions.eq;
import static org.hibernate.criterion.Restrictions.gt;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.type.LongType;
import org.hibernate.type.Type;
import org.joda.time.DateTime;

import br.com.caelum.brutal.dto.KarmaByQuestionHistory;
import br.com.caelum.brutal.dto.UserSummaryForTag;
import br.com.caelum.brutal.model.ReputationEvent;
import br.com.caelum.brutal.model.Tag;
import br.com.caelum.brutal.model.User;
import br.com.caelum.vraptor.ioc.Component;

@Component
public class ReputationEventDAO {

	private static final int TOP_ANSWERERS = 20;
	private final Session session;
	private final InvisibleForUsersRule invisible;

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
	public KarmaByQuestionHistory karmaWonByQuestion(User user, DateTime after, Integer maxResults) {
		Criteria criteria = karmaByQuestionCriteria(user, after).setMaxResults(maxResults);
		return new KarmaByQuestionHistory(criteria.list());
	}

	@SuppressWarnings("unchecked")
	public KarmaByQuestionHistory karmaWonByQuestion(User user,
			DateTime after) {
		Criteria criteria = karmaByQuestionCriteria(user, after);
		return new KarmaByQuestionHistory(criteria.list());
	}
	
	private Criteria karmaByQuestionCriteria(User user, DateTime after) {
		Criteria criteria = session.createCriteria(ReputationEvent.class, "e")
				.createAlias("e.user", "u")
				.createAlias("e.context", "c")
				.setProjection(projectionList()
					.add(alias(property("e.context"), "c"))
					.add(sum("e.karmaReward"))
					.add(property("date"))
					.add(sqlGroupProjection(
					    "day(date) as day", 
					    "day(date)", 
					    new String[]{"day"}, 
					    new Type[] {new LongType()}
					    )
					 )
					 .add(groupProperty("c.id"))
				)
				.add(and(
					eq("u.id", user.getId()), 
					gt("e.date", after)
					)
				)
				.addOrder(desc("e.date"));
		return addInvisibleFilter(criteria);
	}

	
	@SuppressWarnings("unchecked")
	public KarmaByQuestionHistory karmaWonByQuestion(User user) {
		String hql = "select question, sum(e.karmaReward), e.date from ReputationEvent e " +
				"join e.user u left join e.context question " +
				"where u=:user " +
				"group by question, day(e.date) " +
				"order by e.date desc";
		
		Query query = session.createQuery(hql).setParameter("user", user);
		return new KarmaByQuestionHistory(query.list());
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
		String hql = "select new br.com.caelum.brutal.dto.UserSummaryForTag(sum(e.karmaReward) as karmaSum, count(distinct a), user) from ReputationEvent e "+
				"join e.user user "+
				"join e.context c "+
				"join c.answers a "+
				"join c.information.tags t "+
				"where user=a.author and e.type in (:events)" + where + "and t=:tag "+
				"group by user "+
				"order by karmaSum desc";
		
		return session.createQuery(hql).setParameterList("events", ANSWERER_RELATED_EVENTS()).setParameter("tag", tag).setMaxResults(TOP_ANSWERERS);
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
		String hql = "select new br.com.caelum.brutal.dto.UserSummaryForTag(sum(e.karmaReward) as karmaSum, count(distinct c), user) from ReputationEvent e "+
				"join e.user user "+
				"join e.context c "+
				"join c.information.tags t "+
				"where e.type in (:events)" + where + "and t=:tag "+
				"group by user "+
				"order by karmaSum desc";
		
		return session.createQuery(hql).setParameterList("events", ASKER_RELATED_EVENTS()).setParameter("tag", tag).setMaxResults(TOP_ANSWERERS);
	}
	
	private Criteria addInvisibleFilter(Criteria criteria){
		return invisible.addFilter("q", criteria);
	}
	
}
