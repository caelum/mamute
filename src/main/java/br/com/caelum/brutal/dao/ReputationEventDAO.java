package br.com.caelum.brutal.dao;

import org.hibernate.Query;
import org.hibernate.Session;

import br.com.caelum.brutal.model.EventType;
import br.com.caelum.brutal.model.Question;
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
		if (reputationEvent != ReputationEvent.VOID_EVENT)
			session.save(reputationEvent);
	}

	public void delete(ReputationEvent event) {
		Query query = session.createQuery("delete ReputationEvent e where e.type=:type and e.questionInvolved=:question and e.user=:user");
		query.setParameter("type", event.getType())
			.setParameter("question", event.getQuestionInvolved())
			.setParameter("user", event.getUser())
			.executeUpdate();
	}

	public ReputationEvent find(EventType type, User user,
			Question question) {
		Query query = session.createQuery("select e ReputationEvent e where e.type=:type and e.questionInvolved=:question and e.user=:user");
		return (ReputationEvent) query.setParameter("type", type)
			.setParameter("user", user)
			.setParameter("question", question).uniqueResult();
	}

}
