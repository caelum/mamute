package org.mamute.dao;

import br.com.caelum.vraptor.environment.DefaultEnvironment;
import br.com.caelum.vraptor.environment.Environment;
import br.com.caelum.vraptor.ioc.cdi.CDIBasedContainer;
import br.com.caelum.vraptor.test.container.CdiContainer;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.junit.After;
import org.junit.Before;
import org.mamute.model.User;
import org.mamute.model.interfaces.Identifiable;

import javax.enterprise.inject.spi.CDI;
import javax.validation.ValidatorFactory;
import java.io.IOException;

import static br.com.caelum.vraptor.environment.EnvironmentType.TEST;
import static br.com.caelum.vraptor.environment.ServletBasedEnvironment.ENVIRONMENT_PROPERTY;

@SuppressWarnings("unchecked")
public abstract class DatabaseTestCase extends TestCase {

    protected Session session;
    protected User loggedUser;

    private static final SessionFactory factory;

    static {
        try {
            System.setProperty(ENVIRONMENT_PROPERTY, "test");
            CdiContainer cdiContainer = new CdiContainer();
            cdiContainer.start();
            Environment testing = new DefaultEnvironment(TEST);
            CDIBasedContainer cdiBasedContainer = CDI.current().select(CDIBasedContainer.class).get();
            ValidatorFactory vf = cdiBasedContainer.instanceFor(ValidatorFactory.class);
            factory =  cdiBasedContainer.instanceFor(SessionFactory.class);
//            creator = new SessionFactoryCreator(testing, vf);
//            creator.init();
//            factory = creator.getInstance();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    protected <T> T save(T obj) {
        session.save(obj);
        return obj;
    }

    @Before
    public void beforeDatabase() {
        session = factory.openSession();
        session.beginTransaction();
    }

    @After
    public void afterDatabase() {
        boolean wasActive = session.getTransaction().isActive();
        if (wasActive) {
            session.getTransaction().rollback();
        }
        session.close();
    }

    public <T extends Identifiable> T reload(T obj) {
        session.evict(obj);
        return (T) session.load(obj.getClass(), obj.getId());
    }
}
