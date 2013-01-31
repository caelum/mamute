package br.com.caelum.pagpag.cenario.validacao;

import static org.junit.Assert.assertTrue;

import java.io.IOException;

import org.apache.commons.httpclient.HttpException;
import org.junit.Test;

import br.com.caelum.pagpag.aceitacao.util.AcceptanceTestBase;
import br.com.caelum.pagpag.pages.FormularioDadosPagador;
import br.com.caelum.pagpag.pages.FormularioFormaPagamento;
import br.com.caelum.pagpag.test.utils.RecursoTeste;

public class DadosDoTitularDoCartaoTest extends AcceptanceTestBase {

	@Test
	public void usuarioNaoPreencheDadosDoTitularDoCartao() throws HttpException, IOException, InterruptedException {
		FormularioDadosPagador formPagador = pagpag().postComXMLDoPagamento(RecursoTeste.XML_INICIAR_PAGAMENTO_SEM_REPRESENTANTE);
		FormularioFormaPagamento formPagamento = formPagador.getAbaDeFormaDePagamento();
		
		compradorVazio(formPagamento);
		formPagamento.preencheESubmita();
		
		assertTrue(formPagamento.validacoesDoPagadorEstaoVisiveis());
	}
	
	private void compradorVazio(FormularioFormaPagamento formPagamento) {
		formPagamento.ignorarCpfTitular();
		formPagamento.ignorarNascimentoPortador();
		formPagamento.ignorarNomePortador();
		formPagamento.ignorarTelefonePortador();
		formPagamento.ignorarDDDPortador();
	}


	
}
