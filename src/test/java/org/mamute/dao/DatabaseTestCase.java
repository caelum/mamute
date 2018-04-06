package org.mamute.dao;

import static br.com.caelum.vraptor.environment.EnvironmentType.TEST;

import java.io.IOException;

import javax.validation.ValidatorFactory;

import org.apache.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.junit.After;
import org.junit.Before;
import org.mamute.model.User;
import org.mamute.model.interfaces.Identifiable;
import org.mamute.providers.MamuteDatabaseConfiguration;
import org.mamute.providers.SessionFactoryCreator;
import org.mamute.testcase.CDITestCase;

import br.com.caelum.vraptor.environment.DefaultEnvironment;
import br.com.caelum.vraptor.environment.Environment;

@SuppressWarnings("unchecked")
public abstract class DatabaseTestCase extends CDITestCase{
	private static Logger LOG = Logger.getLogger(DatabaseTestCase.class);
	protected static final SessionFactory factory;
	private static final SessionFactoryCreator creator;
	protected Session session;
	protected User loggedUser;

	static {
		try {
			ValidatorFactory vf = cdiBasedContainer.instanceFor(ValidatorFactory.class);
			Environment testing = new DefaultEnvironment(TEST);
			MamuteDatabaseConfiguration configuration = new MamuteDatabaseConfiguration(testing, vf, null);
			configuration.init();
			creator = new SessionFactoryCreator(configuration);
			LOG.info("Initializing database (SessionFactoryCreator) ...");
			creator.init();
			factory = creator.getInstance();
			LOG.info("Database initialized.");
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
