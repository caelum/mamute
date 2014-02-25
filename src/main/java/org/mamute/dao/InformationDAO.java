package org.mamute.dao;

import java.util.Arrays;
import java.util.List;

import javax.inject.Inject;

import org.hibernate.Query;
import org.hibernate.Session;
import org.mamute.model.AnswerInformation;
import org.mamute.model.Information;
import org.mamute.model.ModeratableAndPendingHistory;
import org.mamute.model.Question;
import org.mamute.model.QuestionInformation;
import org.mamute.model.UpdateStatus;

public class InformationDAO {
	private static final String MODEL_PACKAGE = "org.mamute.model.";

    private Session session;

    @Deprecated
    public InformationDAO() {
	}

    @Inject
    public InformationDAO(Session session) {
        this.session = session;
    }

    public List<Information> pendingFor(Long questionId, Class<?> clazz) {
        return selectUpdatableWithStatus(questionId, clazz, Arrays.asList(UpdateStatus.PENDING));
    }

    public List<Information> historyForQuestion(Long questionId) {
        return selectUpdatableWithStatus(questionId, Question.class, Arrays.asList(UpdateStatus.APPROVED, UpdateStatus.NO_NEED_TO_APPROVE));
    }
    
    @SuppressWarnings("unchecked")
	private List<Information> selectUpdatableWithStatus(Long questionId, Class<?> clazz, List<UpdateStatus> status) {
		String hql = "select info from " + clazz.getSimpleName() + " updatable " +
                "join updatable.history info " +
                "where updatable.id=:id and info.status in :status";
        return session.createQuery(hql)
                .setParameter("id", questionId)
                .setParameterList("status", status)
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
