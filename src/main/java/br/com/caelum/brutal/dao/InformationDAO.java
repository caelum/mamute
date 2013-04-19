package br.com.caelum.brutal.dao;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;

import br.com.caelum.brutal.model.Information;
import br.com.caelum.brutal.model.ModeratableAndPendingHistory;
import br.com.caelum.brutal.model.UpdateStatus;
import br.com.caelum.vraptor.ioc.Component;

@Component
public class InformationDAO {

    private final Session session;

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

}
