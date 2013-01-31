package br.com.caelum.pagpag.integracao.dao;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

import org.hibernate.Session;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;

public abstract class DatabaseTestCase {

	private static EntityManagerFactory emf;
	protected EntityManager em;
	protected Session session;
	
	@BeforeClass
	public static void beforeAll() {
		emf = new br.com.caelum.pagpag.integracao.util.EntityManagerFactory().getFactory();
	}
	
	@AfterClass
	public static void afterAll() {
		emf.close();
	}

	@Before
	public void criaEm() {
		em = emf.createEntityManager();
		session = (Session) em.getDelegate();
		em.getTransaction().begin();
	}

	@After
	public void fim() {
		em.getTransaction().rollback();
	}

	protected void flushAndClear() {
		em.flush();
		em.clear();
	}
	

}
