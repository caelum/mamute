package br.com.caelum.brutal.dao;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;

import br.com.caelum.brutal.model.QuestionAndPendingHistory;
import br.com.caelum.brutal.model.QuestionInformation;
import br.com.caelum.brutal.model.UpdateStatus;
import br.com.caelum.vraptor.ioc.Component;

@Component
public class QuestionInformationDAO {
	
	private final Session session;

	public QuestionInformationDAO(Session session) {
		this.session = session;
	}

	@SuppressWarnings("unchecked")
	public QuestionAndPendingHistory pending() {
		String hql = "select question, question_info from Question question " +
				"join question.history question_info " +
				"where question_info.status = :pending order by question_info.createdAt asc";
		Query query = session.createQuery(hql);
		query.setParameter("pending", UpdateStatus.PENDING);
		List<Object[]> results = query.list();
		QuestionAndPendingHistory pending = new QuestionAndPendingHistory(results);
		return pending;
	}

	@SuppressWarnings("unchecked")
	public List<QuestionInformation> allSimilarTo(Long id) {
	//	UpdateHistory history = (UpdateHistory) session.load(UpdateHistory.class, id);
		//String hql = "select qi from QuestionInformation qi where qi.status = :pending and q.id = :id and h.type = :type";
		//Query query = session.createQuery(hql);
		//return query.setParameter("pending", UpdateStatus.PENDING).setParameter("id", history.getTargetId()).list();
		return null;
	}


}
