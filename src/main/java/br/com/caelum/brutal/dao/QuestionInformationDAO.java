package br.com.caelum.brutal.dao;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;

import br.com.caelum.brutal.model.QuestionInformation;
import br.com.caelum.brutal.model.UpdatablesAndPendingHistory;
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
	public List<QuestionInformation> from(Long questionId) {
	    String hql = "select question_info from Question " +
	    		"question join fetch question.history question_info " +
	    		"where question.id=:id";
	    
	    return session.createQuery(hql).setParameter("id", questionId).list();
	}


}
