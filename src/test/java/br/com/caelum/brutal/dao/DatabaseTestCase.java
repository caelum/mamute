package br.com.caelum.brutal.dao;

import java.io.IOException;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.junit.After;
import org.junit.Before;

import br.com.caelum.brutal.model.User;
import br.com.caelum.brutal.model.interfaces.Identifiable;
import br.com.caelum.brutal.providers.SessionFactoryCreator;
import br.com.caelum.vraptor.environment.DefaultEnvironment;
import br.com.caelum.vraptor.environment.Environment;

@SuppressWarnings("unchecked")
public abstract class DatabaseTestCase extends TestCase {
	private static final SessionFactory factory;
	private static final SessionFactoryCreator creator;
	protected Session session;
	protected User loggedUser;

	static {
		try {
			Environment testing = new DefaultEnvironment("testing");
			creator = new SessionFactoryCreator(testing);
			factory = creator.getInstance();
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
