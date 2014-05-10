package org.mamute.providers;

import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

import javax.inject.Inject;

public class SessionFactoryProvider {

    @Inject
    private SessionFactory sessionFactory;

    @Inject
    private Configuration cfg;


//    public void dropAndCreate() {
//        destroy(this.sessionFactory);
//        new SchemaExport(cfg).drop(true, true);
//        new SchemaExport(cfg).create(true, true);
////        init();
//    }
//
//    public void drop() {
//        sessionFactory.close();
//        sessionFactory = null;
//        new SchemaExport(cfg).drop(true, true);
//    }
}
