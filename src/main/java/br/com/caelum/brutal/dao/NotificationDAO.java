package br.com.caelum.brutal.dao;

import org.hibernate.Session;

import br.com.caelum.brutal.model.Notification;
import br.com.caelum.vraptor.ioc.Component;

@Component
public class NotificationDAO {
    
    private final Session session; 
    
    public NotificationDAO(Session session) {
        this.session = session;
    }

    public void save(Notification notification) {
        session.save(notification);
    }

}
