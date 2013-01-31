package br.com.caelum.pagpag.integracao.util;

import javax.persistence.EntityManager;
import javax.persistence.Persistence;

public class EntityManagerFactory {

	private final javax.persistence.EntityManagerFactory factory = Persistence
			.createEntityManagerFactory("test");

	public EntityManager deTeste() {
		return factory.createEntityManager();
	}
	
	public javax.persistence.EntityManagerFactory getFactory() {
		return factory;
	}

	public void close() {
		factory.close();
	}
}
