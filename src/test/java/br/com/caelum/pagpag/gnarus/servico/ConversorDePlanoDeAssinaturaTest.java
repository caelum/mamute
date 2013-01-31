package br.com.caelum.pagpag.gnarus.servico;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import br.com.caelum.pagpag.gnarus.modelo.PeriodoDeAssinatura;
import br.com.caelum.pagpag.modelo.dao.CarrinhoDAO;
import br.com.caelum.pagpag.modelo.dominio.Carrinho;
import br.com.caelum.pagpag.modelo.dominio.Item;
import br.com.caelum.pagpag.modelo.dominio.builders.CarrinhoBuilder;

public class ConversorDePlanoDeAssinaturaTest {

	private ConversorDePlanoDeAssinatura conversor;
	private CarrinhoDAO dao;

	@Before
	public void setUp() {
		dao = mock(CarrinhoDAO.class);
		conversor = new ConversorDePlanoDeAssinatura(dao);
	}
	
	@Test
	public void deveEvoluirParaAssinaturaDe6Meses() {
		
		Carrinho carrinho = carrinhoComAssinaturaPadrao();
		
		Carrinho novoCarrinho = conversor.evolui(carrinho, PeriodoDeAssinatura.SEMESTRAL);
		
		assertEquals(1, novoCarrinho.getItens().size());
		assertEquals(PeriodoDeAssinatura.SEMESTRAL.getCodigo(), novoCarrinho.getItens().get(0).getCodigo());
	}
	
	@Test
	public void deveEvoluirParaAssinaturaDe1Ano() {
		
		Carrinho carrinho = carrinhoComAssinaturaPadrao();
		
		Carrinho novoCarrinho = conversor.evolui(carrinho, PeriodoDeAssinatura.ANUAL);
		
		assertEquals(1, novoCarrinho.getItens().size());
		assertEquals(PeriodoDeAssinatura.ANUAL.getCodigo(), novoCarrinho.getItens().get(0).getCodigo());
	}

	@Test
	public void deveEvoluirParaAssinaturaDe2Anos() {
		
		Carrinho carrinho = carrinhoComAssinaturaPadrao();
		
		Carrinho novoCarrinho = conversor.evolui(carrinho, PeriodoDeAssinatura.BIANUAL);
		
		assertEquals(1, novoCarrinho.getItens().size());
		assertEquals(PeriodoDeAssinatura.BIANUAL.getCodigo(), novoCarrinho.getItens().get(0).getCodigo());
	}
	
	@Test
	public void devePersistirONovoItem() {
		
		Carrinho carrinho = carrinhoComAssinaturaPadrao();
		
		Carrinho novoCarrinho = conversor.evolui(carrinho, PeriodoDeAssinatura.SEMESTRAL);
		
		verify(dao).salvar(novoCarrinho.getItens().get(0));
	}
	
	private Carrinho carrinhoComAssinaturaPadrao() {
		
		List<Item> itens = new ArrayList<Item>();
		itens.add(PeriodoDeAssinatura.SEMESTRAL.item());
		
		return new CarrinhoBuilder()
			.paraOs(itens)
			.build();
		
	}
}