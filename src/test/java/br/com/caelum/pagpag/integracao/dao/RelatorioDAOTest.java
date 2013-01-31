package br.com.caelum.pagpag.integracao.dao;

import static org.junit.Assert.assertEquals;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Calendar;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import br.com.caelum.pagpag.modelo.dao.RelatorioDAO;
import br.com.caelum.pagpag.modelo.dominio.Carrinho;
import br.com.caelum.pagpag.modelo.dominio.FormaPagamento;
import br.com.caelum.pagpag.modelo.dominio.Item;
import br.com.caelum.pagpag.modelo.dominio.Pagamento;
import br.com.caelum.pagpag.modelo.dominio.Representante;
import br.com.caelum.pagpag.modelo.dominio.StatusPagamento;
import br.com.caelum.pagpag.modelo.relatorio.VendaEfetuada;


public class RelatorioDAOTest extends DatabaseTestCase {

	private RelatorioDAO dao;
	
	@Before
	public void configura() {
		dao = new RelatorioDAO(em);
	}

	@Test
	public void deveRetornarVendasDoDia() {
		
		Carrinho carrinho = carrinhoCom(
				new Item("OO em java", new BigDecimal("100.0"), "FJ-11"),
				new Item("OO em java para quem veio de scala", new BigDecimal("200.0"), "FJ-12"));
		
		Pagamento p = new Pagamento(new Representante("33059199817"), carrinho, FormaPagamento.ITAU);
		p.setStatus(StatusPagamento.AUTORIZADO, "", "", "");
		
		em.persist(p);
		
		flushAndClear();
		
		List<VendaEfetuada> vendas = dao.vendasNoDia(Calendar.getInstance());
		
		assertEquals(2, vendas.size());
		assertEquals(new VendaEfetuada(new BigDecimal("100.0"), "FJ-11", new BigInteger("1")), vendas.get(0));
		assertEquals(new VendaEfetuada(new BigDecimal("200.0"), "FJ-12", new BigInteger("1")), vendas.get(1));
	}
	
	@Test
	public void deveRetornarVendasAcumuladasDoDia() {
		
		Carrinho carrinho = carrinhoCom(
				new Item("OO em java", new BigDecimal("100.0"), "FJ-11"),
				new Item("OO em java", new BigDecimal("100.0"), "FJ-11"),
				new Item("OO em java para quem veio de scala", new BigDecimal("200.0"), "FJ-12"));
		
		em.persist(carrinho);
		
		Pagamento p = new Pagamento(new Representante("33059199817"), carrinho, FormaPagamento.ITAU);
		p.setStatus(StatusPagamento.AUTORIZADO, "", "", "");
		
		em.persist(p);
		
		flushAndClear();
		
		List<VendaEfetuada> vendas = dao.vendasNoDia(Calendar.getInstance());
		
		assertEquals(2, vendas.size());
		assertEquals(new VendaEfetuada(new BigDecimal("100.0"), "FJ-11", new BigInteger("2")), vendas.get(0));
		assertEquals(new VendaEfetuada(new BigDecimal("200.0"), "FJ-12", new BigInteger("1")), vendas.get(1));
	}
	
	@Test
	public void deveRetornarVendasAcumuladasDoDiaMesmoQueConcluidaNoMesmoDia() {
		
		Carrinho carrinho = carrinhoCom(
				new Item("OO em java", new BigDecimal("100.0"), "FJ-11"),
				new Item("OO em java", new BigDecimal("100.0"), "FJ-11"),
				new Item("OO em java para quem veio de scala", new BigDecimal("200.0"), "FJ-12"));
		
		em.persist(carrinho);
		
		Pagamento p = new Pagamento(new Representante("33059199817"), carrinho, FormaPagamento.ITAU);
		p.setStatus(StatusPagamento.AUTORIZADO, "", "", "");
		p.setStatus(StatusPagamento.CONCLUIDO, "", "", "");
		
		em.persist(p);
		
		flushAndClear();
		
		List<VendaEfetuada> vendas = dao.vendasNoDia(Calendar.getInstance());
		
		assertEquals(2, vendas.size());
		assertEquals(new VendaEfetuada(new BigDecimal("100.0"), "FJ-11", new BigInteger("2")), vendas.get(0));
		assertEquals(new VendaEfetuada(new BigDecimal("200.0"), "FJ-12", new BigInteger("1")), vendas.get(1));
	}
	
}
