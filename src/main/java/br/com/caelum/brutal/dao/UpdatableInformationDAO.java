package br.com.caelum.brutal.dao;

import java.util.List;

import org.hibernate.Session;

import br.com.caelum.brutal.model.Information;
import br.com.caelum.brutal.model.UpdateStatus;
import br.com.caelum.brutal.model.interfaces.Updatable;
import br.com.caelum.vraptor.ioc.Component;

@Component
public class UpdatableInformationDAO {

    private final Session session;

    public UpdatableInformationDAO(Session session) {
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

    public Information getUpdatableInfoById(Long id, Class<?> clazz) {
        return (Information) session.load(clazz, id);
    }

    public Updatable getUpdatableById(Long id, Class<?> clazz) {
        return (Updatable) session.load(clazz, id);
    }


}
