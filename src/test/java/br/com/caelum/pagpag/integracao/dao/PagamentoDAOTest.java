package br.com.caelum.pagpag.integracao.dao;

import static org.junit.Assert.assertEquals;

import java.lang.reflect.Field;
import java.util.Calendar;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import br.com.caelum.pagpag.modelo.dao.PagamentoDAO;
import br.com.caelum.pagpag.modelo.dominio.FormaPagamento;
import br.com.caelum.pagpag.modelo.dominio.Pagamento;
import br.com.caelum.pagpag.modelo.dominio.Representante;
import br.com.caelum.pagpag.modelo.dominio.StatusPagamento;
import br.com.caelum.pagpag.util.Dia;

public class PagamentoDAOTest {
	private EntityManager em;
	private PagamentoDAO dao;

	@Before
	public void criaEm() {
		EntityManagerFactory emf = Persistence.createEntityManagerFactory("test");
		em = emf.createEntityManager();
		em.getTransaction().begin();

		dao = new PagamentoDAO(em);
	}

	@After
	public void fim() {
		em.getTransaction().rollback();
	}

	@Test
	public void deveRetornarApenasOsPagamentosDeBoletoVencidosNoPenultimoDia() {
		Representante diego = new Representante("Diego", "diego@caelum.com.br", "11111111111", null, null, null);
		Representante roberto = new Representante("Roberto Marinho", "roberto@marinho.com", "22222222222", null, null, null);
		Calendar anteOntem = new Dia().anteOntem().getData();

		Pagamento boletoVencido = criarPagamento(diego, StatusPagamento.BOLETO, FormaPagamento.BOLETO);
		alterarDataDePagamento(boletoVencido, anteOntem);
		dao.salvar(boletoVencido);
		Pagamento outroBoletoVencido = criarPagamento(roberto, StatusPagamento.BOLETO, FormaPagamento.BOLETO);
		alterarDataDePagamento(outroBoletoVencido, anteOntem);
		dao.salvar(outroBoletoVencido);
		
		Pagamento cartaoVencido = criarPagamento(diego, StatusPagamento.BOLETO, FormaPagamento.VISA);
		alterarDataDePagamento(cartaoVencido, anteOntem);
		dao.salvar(cartaoVencido);
		Pagamento boletoEmDia = criarPagamento(diego, StatusPagamento.BOLETO, FormaPagamento.BOLETO);
		dao.salvar(boletoEmDia);
		Pagamento boletoAutorizado = criarPagamento(diego, StatusPagamento.AUTORIZADO, FormaPagamento.BOLETO);
		dao.salvar(boletoAutorizado);
		
		finalizarInteracaoComOBanco();
		
		List<Pagamento> boletosVencidosAnteOntem = dao.boletosVencidosAnteOntem();
		assertEquals(2, boletosVencidosAnteOntem.size());
		assertEquals(boletoVencido.getId(), boletosVencidosAnteOntem.get(0).getId());
		assertEquals(outroBoletoVencido.getId(), boletosVencidosAnteOntem.get(1).getId());
	}

	private Pagamento criarPagamento(Representante representante, StatusPagamento status, FormaPagamento forma) {
		Pagamento pagamento = new Pagamento();
		pagamento.setStatus(status, "", "", "");
		pagamento.setForma(forma);
		pagamento.setRepresentante(representante);
		return pagamento;
	}

	private void alterarDataDePagamento(Pagamento pagamento, Calendar data){
		try {
			Field dataCriacao = pagamento.getClass().getDeclaredField("dataCriacao");
			dataCriacao.setAccessible(true);
			dataCriacao.set(pagamento, data);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	private void finalizarInteracaoComOBanco() {
		em.flush();
		em.clear();
	}
}
