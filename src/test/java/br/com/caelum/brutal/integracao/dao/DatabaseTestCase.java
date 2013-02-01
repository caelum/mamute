package br.com.caelum.brutal.integracao.dao;

import java.io.IOException;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.junit.After;
import org.junit.Before;

import br.com.caelum.brutal.model.User;
import br.com.caelum.brutal.providers.SessionFactoryCreator;
import br.com.caelum.vraptor.environment.DefaultEnvironment;
import br.com.caelum.vraptor.environment.Environment;

public abstract class DatabaseTestCase {

	private static final SessionFactory factory;

	private static final SessionFactoryCreator creator;
	static {
		try {
			Environment testing = new DefaultEnvironment("testing");
			creator = new SessionFactoryCreator(testing);
			factory = creator.getInstance();
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}


	protected Session session;
	protected User loggedUser;

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


}
