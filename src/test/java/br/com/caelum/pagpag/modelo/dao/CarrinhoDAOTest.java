package br.com.caelum.pagpag.modelo.dao;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import br.com.caelum.pagpag.integracao.dao.DatabaseTestCase;
import br.com.caelum.pagpag.modelo.dominio.Carrinho;
import br.com.caelum.pagpag.modelo.dominio.FormaPagamento;
import br.com.caelum.pagpag.modelo.dominio.Item;
import br.com.caelum.pagpag.modelo.dominio.Pagamento;
import br.com.caelum.pagpag.modelo.dominio.Representante;
import br.com.caelum.pagpag.modelo.dominio.StatusPagamento;
import br.com.caelum.pagpag.util.Dia;

public class CarrinhoDAOTest extends DatabaseTestCase {

	private static final String CPF = "22464454001";
	private Carrinho carrinho;
	private CarrinhoDAO dao;

	@Before
	public void setup() {
		this.carrinho = carrinhoCom(new Item("OO em java", 
				new BigDecimal("100.0"), "FJ-11"));
		carrinho.getRepresentante().setCpf(CPF);

		dao = new CarrinhoDAO(em);
	}
	@Test
	public void deve_trazer_as_que_nao_foram_finalizadas_mas_deve_ignorar_as_finalizadas() {
		flushAndClear();
		Calendar de = new Dia(Calendar.getInstance()).comecoDoDia().getData();
		Calendar ate = new Dia(Calendar.getInstance()).fimDoDia().getData();
		assertThat(dao.naoFinalizados(de, ate).size(), is(1));
		assertThat(dao.naoFinalizados(de, ate ).get(0).getId(), is(carrinho.getId()));
		paymentFor(carrinho);
		flushAndClear();
		assertTrue(dao.naoFinalizados(de, ate).isEmpty());
	}
	
	@Test
	public void nao_deve_trazer_carrinhos_cujo_usuario_ja_fez_uma_compra_antes() {
		Calendar de = new Dia(Calendar.getInstance()).comecoDoDia().getData();
		Calendar ate = new Dia(Calendar.getInstance()).fimDoDia().getData();
		assertThat(dao.naoFinalizados(de, ate).size(), is(1));

		Carrinho carrinhoPagoAntes = carrinhoCom(new Item("Scala", 
				new BigDecimal("150.0"), "FJ-13"));
		carrinhoPagoAntes.getRepresentante().setCpf(CPF);
		paymentFor(carrinhoPagoAntes);

		flushAndClear();
		assertTrue(dao.naoFinalizados(de, ate).isEmpty());
		
	}

	@Test
	public void deve_filtrar_somenete_data_passada(){
		Calendar quatroDiasAtras = new Dia().subitrairDias(4).getData();
		Carrinho carrinho2 = new Carrinho(quatroDiasAtras,"Joao","carrinhoTest", new ArrayList<Item>(),new BigDecimal(0.05) , new Representante("52771023962"), null );
		carrinho2.setIdNoConsumidor("24242");
		dao.salvar(carrinho2);
		List<Carrinho> carrinhos = dao.naoFinalizados(new Dia().subtrairMes(6).getData(), new Dia().subitrairDias(3).getData());
		assertThat(carrinhos.size(), is(1));
		assertThat(carrinhos.get(0).getId(), is(carrinho2.getId()));	
		
	}
	
	@Test
	public void deve_trazer_o_carrinho_pelo_id(){
		Carrinho carrinhoTeste = dao.findCarrinhoPeloId(carrinho.getId());
		assertThat(carrinhoTeste.getId(), is(carrinho.getId()));	
	}
		

	private Pagamento paymentFor(Carrinho carrinho) {
		Pagamento p = new Pagamento(new Representante(CPF), carrinho,
				FormaPagamento.ITAU);
		p.setStatus(StatusPagamento.AUTORIZADO, "", "", "");
		em.persist(p);
		return p;
	}
	
	
}
