package br.com.caelum.brutal.dao;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;

import br.com.caelum.brutal.model.Updatable;
import br.com.caelum.brutal.model.UpdateHistory;
import br.com.caelum.brutal.model.UpdateStatus;
import br.com.caelum.vraptor.ioc.Component;

@Component
@SuppressWarnings("rawtypes")
public class HistoryDAO {

	private final Session session;

	public HistoryDAO(Session session) {
		this.session = session;
	}

	public Updatable load(Class type, Long id) {
		return (Updatable) session.load(type, id);
	}

	public void save(UpdateHistory history) {
		this.session.save(history);
	}

	@SuppressWarnings("unchecked")
	public List<UpdateHistory> unmoderated() {
		String hql = "from UpdateHistory where status = :pending order by createdAt asc";
		Query query = session.createQuery(hql);
		return query.setParameter("pending", UpdateStatus.PENDING).list();
	}

	@SuppressWarnings("unchecked")
	public List<UpdateHistory> allSimilarTo(Long id) {
		UpdateHistory history = (UpdateHistory) session.load(UpdateHistory.class, id);
		String hql = "select h from UpdateHistory as h where h.status = :pending and q.id = :id and h.type = :type";
		Query query = session.createQuery(hql);
		return query.setParameter("pending", UpdateStatus.PENDING).setParameter("id", history.getTargetId()).list();
	}

}
