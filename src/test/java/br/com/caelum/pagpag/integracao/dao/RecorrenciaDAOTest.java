package br.com.caelum.pagpag.integracao.dao;

import static org.junit.Assert.assertEquals;

import java.math.BigDecimal;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import br.com.caelum.pagpag.modelo.dao.RecorrenciaDAO;
import br.com.caelum.pagpag.modelo.dominio.Carrinho;
import br.com.caelum.pagpag.modelo.dominio.FormaPagamento;
import br.com.caelum.pagpag.modelo.dominio.Item;
import br.com.caelum.pagpag.modelo.dominio.Pagamento;
import br.com.caelum.pagpag.modelo.dominio.Representante;
import br.com.caelum.pagpag.modelo.dominio.StatusPagamento;
import br.com.caelum.pagpag.modelo.moip.ServicoMoip;
import br.com.caelum.pagpag.util.Dia;

public class RecorrenciaDAOTest extends DatabaseTestCase {

	private RecorrenciaDAO dao;
	private Pagamento pagamento;
	private Carrinho carrinho;

	@Before
	public void configuraCarrinhoEPagamento() {
		dao = new RecorrenciaDAO(em);
		this.carrinho = carrinhoCom(new Item("OO em java", new BigDecimal(
				"100.0"), "FJ-11"), new Item(
				"OO em java para quem veio de scala", new BigDecimal("200.0"),
				"FJ-12"));
		this.pagamento = new Pagamento(new Representante("33059199817"), carrinho,
				FormaPagamento.ITAU);
	}

	@Test
	public void deveIgnorarOsNaoPagos() {

		em.persist(pagamento);
		em.persist(new ServicoMoip(pagamento, ""));

		List<Pagamento> vencendo = dao.getAssinaturasVencendo(carrinho
				.getRecorrencia().proximaData());

		assertEquals(0, vencendo.size());
	}

	@Test
	public void deveTrazerOsVencendoNaData() {

		pagamento.setStatus(StatusPagamento.AUTORIZADO, "", "", "");

		em.persist(pagamento);
		em.persist(new ServicoMoip(pagamento, ""));

		List<Pagamento> vencendo = dao.getAssinaturasVencendo(carrinho
				.getRecorrencia().proximaData());

		assertEquals(1, vencendo.size());
		assertEquals(pagamento, vencendo.get(0));
	}

	@Test
	public void deveIgnorarVencendoOntemOuAmanha() {

		pagamento.setStatus(StatusPagamento.AUTORIZADO, "", "", "");

		em.persist(pagamento);
		em.persist(new ServicoMoip(pagamento, ""));

		List<Pagamento> vencendoOntem = dao.getAssinaturasVencendo(new Dia(carrinho
				.getRecorrencia().proximaData()).ontem().getData());
		assertEquals(0, vencendoOntem.size());
		List<Pagamento> vencendoAmanha = dao.getAssinaturasVencendo(new Dia(carrinho
				.getRecorrencia().proximaData()).somaDias(1).getData());
		assertEquals(0, vencendoAmanha.size());
	}

	@Test
	public void deveIgnorarAssinaturas() {

		pagamento.setStatus(StatusPagamento.AUTORIZADO, "", "", "");

		em.persist(pagamento);
		em.persist(new ServicoMoip(pagamento, "").comoAssinatura());

		List<Pagamento> vencendo = dao.getAssinaturasVencendo(carrinho
				.getRecorrencia().proximaData());
		assertEquals(0, vencendo.size());
	}
}
