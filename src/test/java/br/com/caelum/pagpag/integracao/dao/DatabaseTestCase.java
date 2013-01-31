package br.com.caelum.pagpag.integracao.dao;

import java.util.Arrays;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;

import br.com.caelum.pagpag.modelo.dominio.Carrinho;
import br.com.caelum.pagpag.modelo.dominio.Item;
import br.com.caelum.pagpag.modelo.dominio.Representante;
import br.com.caelum.pagpag.modelo.dominio.builders.CarrinhoBuilder;

public abstract class DatabaseTestCase {

	private static EntityManagerFactory emf;
	protected EntityManager em;
	
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
		em.getTransaction().begin();
	}

	@After
	public void fim() {
		em.getTransaction().rollback();
	}

	protected Carrinho carrinhoCom(Item... itens) {
		Carrinho carrinho = new CarrinhoBuilder().paraOs(Arrays.asList(itens))
		.comDescricao("Pq tem descricao?")
		.feitoNo("GNARUS")
		.comIdNoConsumidor("123")
		.pelo(new Representante(""))
		.build();
		
		for(Item item : carrinho.getItens()) {
			em.persist(item);
		}
	
		em.persist(carrinho);
		return carrinho;
	}

	protected void flushAndClear() {
		em.flush();
		em.clear();
	}
	

}
