package br.com.caelum.brutal.dao;

import java.util.List;

import javax.inject.Inject;

import org.hibernate.Query;
import org.hibernate.Session;

import br.com.caelum.brutal.model.AnswerInformation;
import br.com.caelum.brutal.model.Information;
import br.com.caelum.brutal.model.ModeratableAndPendingHistory;
import br.com.caelum.brutal.model.QuestionInformation;
import br.com.caelum.brutal.model.UpdateStatus;

public class InformationDAO {
	private static final String MODEL_PACKAGE = "br.com.caelum.brutal.model.";

    private Session session;

    @Deprecated
    public InformationDAO() {
	}

    @Inject
    public InformationDAO(Session session) {
        this.session = session;
    }

    @SuppressWarnings("unchecked")
    public List<Information> pendingFor(Long questionId, Class<?> clazz) {
        String hql = "select info from " + clazz.getSimpleName() + " updatable " +
                "join updatable.history info " +
                "where updatable.id=:id and info.status=:pending";
        return session.createQuery(hql)
                .setParameter("id", questionId)
                .setParameter("pending", UpdateStatus.PENDING)
                .list();
    }

	@SuppressWarnings("unchecked")
	public ModeratableAndPendingHistory pendingByUpdatables(Class<?> clazz) {
		String hql = "select updatable, info from "+ clazz.getSimpleName() +" updatable " +
				"join updatable.history info " +
				"where info.status = :pending order by info.createdAt asc";
		Query query = session.createQuery(hql);
		query.setParameter("pending", UpdateStatus.PENDING);
		List<Object[]> results = query.list();
		ModeratableAndPendingHistory pending = new ModeratableAndPendingHistory(results);
		return pending;
	}
	
    public Information getById(Long id, Class<?> clazz) {
        return (Information) session.load(clazz, id);
    }

	public Long pendingCount() {
		Long pendingQuestions = pendingCountFor(QuestionInformation.class);
		Long pendingAnswer = pendingCountFor(AnswerInformation.class);
		return pendingAnswer + pendingQuestions;
	}

	private Long pendingCountFor(Class<? extends Information> clazz) {
		String hql = "select count(*) from " + clazz.getSimpleName() + " qi where qi.status = :pending";
		Long uniqueResult = (Long) session.createQuery(hql).setParameter("pending", UpdateStatus.PENDING).uniqueResult();
		return uniqueResult;
	}

	public Information getById(Long informationId, String typeName) {
		try {
			Class<?> clazz = Class.forName(MODEL_PACKAGE + typeName);
			return getById(informationId, clazz);
		} catch (ClassNotFoundException e) {
			throw new RuntimeException(e);
		}
	}

}
