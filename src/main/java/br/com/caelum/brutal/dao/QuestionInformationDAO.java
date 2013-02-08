package br.com.caelum.brutal.dao;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;

import br.com.caelum.brutal.model.UpdatablesAndPendingHistory;
import br.com.caelum.brutal.model.QuestionInformation;
import br.com.caelum.brutal.model.UpdateStatus;
import br.com.caelum.vraptor.ioc.Component;

@Component
public class QuestionInformationDAO {
	
	private final Session session;

	public QuestionInformationDAO(Session session) {
		this.session = session;
	}
	
	public QuestionInformation getById(Long id) {
	    return (QuestionInformation) session.load(QuestionInformation.class, id);
	}

	@SuppressWarnings("unchecked")
	public UpdatablesAndPendingHistory pending() {
		String hql = "select question, question_info from Question question " +
				"join question.history question_info " +
				"where question_info.status = :pending order by question_info.createdAt asc";
		Query query = session.createQuery(hql);
		query.setParameter("pending", UpdateStatus.PENDING);
		List<Object[]> results = query.list();
		UpdatablesAndPendingHistory pending = new UpdatablesAndPendingHistory(results);
		return pending;
	}

	@SuppressWarnings("unchecked")
	public List<QuestionInformation> from(Long questionId) {
	    String hql = "select question_info from Question " +
	    		"question join fetch question.history question_info " +
	    		"where question.id=:id";
	    
	    return session.createQuery(hql).setParameter("id", questionId).list();
	}

    @SuppressWarnings("unchecked")
    public List<QuestionInformation> pendingFrom(Long questionId) {
        String hql = "select question_info from Question " +
                "question join question.history question_info " +
                "where question.id=:id and question_info.status=:pending";
        return session.createQuery(hql)
                .setParameter("id", questionId)
                .setParameter("pending", UpdateStatus.PENDING)
                .list();
    }


}
