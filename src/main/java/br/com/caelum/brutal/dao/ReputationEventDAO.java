package br.com.caelum.brutal.dao;

import static br.com.caelum.brutal.model.EventType.ANSWERER_RELATED_EVENTS;
import static br.com.caelum.brutal.model.EventType.ASKER_RELATED_EVENTS;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.Session;
import org.joda.time.DateTime;

import br.com.caelum.brutal.dto.KarmaByContextHistory;
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
	public KarmaByContextHistory karmaWonByQuestion(User user, DateTime after, Integer maxResults) {
		Query query = karmaByContextQuery(user, after).setMaxResults(maxResults);
		return new KarmaByContextHistory(query.list());
	}

	@SuppressWarnings("unchecked")
	public KarmaByContextHistory karmaWonByQuestion(User user,
			DateTime after) {
		Query query = karmaByContextQuery(user, after);
		return new KarmaByContextHistory(query.list());
	}
	
	private Query karmaByContextQuery(User user, DateTime after) {
		String hql = "select e.context, sum(e.karmaReward), e.date from ReputationEvent e "+ 
			        "where e.user=:user and e.date > :after " +
			        "group by e.context, day(e.date) " +
			        "order by e.date desc";
				    
	    Query query = session.createQuery(hql).setParameter("user", user).setParameter("after", after);
	    return query;
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
		String hql = "select new br.com.caelum.brutal.dto.UserSummaryForTag(sum(e.karmaReward) as karmaSum, count(distinct a), user) "
				+ "from ReputationEvent e, Question q "+
				"join e.user user "+
				"join q.answers a "+
				"join q.information.tags t "+
				"where user=a.author and e.type in (:events)" + 
				where + 
				"and t=:tag " +
				"and q.id=e.context.id and e.context.class='Question'"+
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
		String hql = "select new "
				+ "br.com.caelum.brutal.dto.UserSummaryForTag(sum(e.karmaReward) as karmaSum, count(distinct q), user) "
				+ "from ReputationEvent e, Question q "+
				"join e.user user "+
				"join q.information.tags t "+
				"where e.type in (:events)" + where + "and t=:tag and e.context.id = q.id and e.context.class='Question' "+
				"group by user "+
				"order by karmaSum desc";
		
		return session.createQuery(hql).setParameterList("events", ASKER_RELATED_EVENTS()).setParameter("tag", tag).setMaxResults(TOP_ANSWERERS);
	}
	
	private Criteria addInvisibleFilter(Criteria criteria){
		return invisible.addFilter("q", criteria);
	}
	
}
