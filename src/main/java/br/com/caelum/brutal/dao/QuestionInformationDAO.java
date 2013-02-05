package br.com.caelum.brutal.dao;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;

import br.com.caelum.brutal.model.QuestionInformation;
import br.com.caelum.brutal.model.UpdateHistory;
import br.com.caelum.brutal.model.UpdateStatus;

public class QuestionInformationDAO {
	
	private final Session session;

	public QuestionInformationDAO(Session session) {
		this.session = session;
	}

	@SuppressWarnings("unchecked")
	public List<QuestionInformation> unmoderated() {
		String hql = "qi from QuestionInformation qi where qi.status = :pending order by createdAt asc";
		Query query = session.createQuery(hql);
		return query.setParameter("pending", UpdateStatus.PENDING).list();
	}

	@SuppressWarnings("unchecked")
	public List<QuestionInformation> allSimilarTo(Long id) {
		UpdateHistory history = (UpdateHistory) session.load(UpdateHistory.class, id);
		String hql = "select qi from QuestionInformation qi where qi.status = :pending and q.id = :id and h.type = :type";
		Query query = session.createQuery(hql);
		return query.setParameter("pending", UpdateStatus.PENDING).setParameter("id", history.getTargetId()).list();
	}


}
