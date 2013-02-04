package br.com.caelum.brutal.dao;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.joda.time.DateTime;

import br.com.caelum.vraptor.ioc.Component;

@Component
public class NotifiableDAO<T> {
    private final Session session;

    public NotifiableDAO(Session session) {
        this.session = session;
    }

    @SuppressWarnings("unchecked")
    public List<T> recent(int hoursAgo, Class<T> clazz) {
        Long milisecAgo = (long) (hoursAgo * (60 * 60 * 1000));  
        DateTime timeAgo = new DateTime(System.currentTimeMillis() - milisecAgo);
        Query query = session.createQuery("select a from " + clazz.getSimpleName() + " a where (a.createdAt) > :timeAgo");
        query.setParameter("timeAgo", timeAgo);
        session.getTransaction().commit();
        return query.list();
    }

}
