package br.com.caelum.brutal.dao;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.joda.time.DateTime;

import br.com.caelum.brutal.dto.KarmaByQuestionHistory;
import br.com.caelum.brutal.model.ReputationEvent;
import br.com.caelum.brutal.model.User;
import br.com.caelum.vraptor.ioc.Component;

@Component
public class ReputationEventDAO {

	private final Session session;

	public ReputationEventDAO(Session session) {
		this.session = session;
	}

	public void save(ReputationEvent reputationEvent) {
		if (reputationEvent.equals(ReputationEvent.IGNORED_EVENT)) {
			session.save(reputationEvent);
		}
	}

	public void delete(ReputationEvent event) {
		Query query = session.createQuery("delete ReputationEvent e where e.type=:type and e.questionInvolved=:question and e.user=:user");
		query.setParameter("type", event.getType())
			.setParameter("question", event.getQuestionInvolved())
			.setParameter("user", event.getUser())
			.executeUpdate();
	}

	@SuppressWarnings("unchecked")
	public KarmaByQuestionHistory karmaWonByQuestion(User user, DateTime after) {
		String hql = "select e.questionInvolved, sum(e.karmaReward) from ReputationEvent e " +
				"join e.user u where u=:user and e.date > :after group by e.questionInvolved";
		
		Query query = session.createQuery(hql);
		List<Object[]> results = query
				.setParameter("user", user)
				.setParameter("after", after).list();
		return new KarmaByQuestionHistory(results);
	}

}
