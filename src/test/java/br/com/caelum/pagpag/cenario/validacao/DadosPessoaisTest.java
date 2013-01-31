package br.com.caelum.pagpag.cenario.validacao;

import static org.junit.Assert.assertTrue;

import java.io.IOException;

import org.apache.commons.httpclient.HttpException;
import org.junit.Test;

import br.com.caelum.pagpag.aceitacao.util.AcceptanceTestBase;
import br.com.caelum.pagpag.modelo.dominio.FormaPagamento;
import br.com.caelum.pagpag.pages.FormularioDadosPagador;
import br.com.caelum.pagpag.pages.FormularioFormaPagamento;
import br.com.caelum.pagpag.test.utils.RecursoTeste;

public class DadosPessoaisTest extends AcceptanceTestBase {

	@Test
	public void usuarioNaoPreencheDadosPessoais() throws HttpException, IOException, InterruptedException {
		FormularioDadosPagador formPagador = pagpag().postComXMLDoPagamento(RecursoTeste.XML_INICIAR_PAGAMENTO_SEM_REPRESENTANTE);
		FormularioFormaPagamento formPagamento = formPagador.getAbaDeFormaDePagamento();
		
		formPagamento.setForma(FormaPagamento.BOLETO).preencheESubmita();
		dadosDoPagadorVazio(formPagador);
		formPagador.preenche();
		formPagamento.preencheESubmita();
		
		assertTrue(formPagador.validacoesEstaoVisiveis());
	}
	
	private void dadosDoPagadorVazio(FormularioDadosPagador formPagador) {
		formPagador.ignorarCpf();
		formPagador.ignorarEmail();
		formPagador.ignorarEstado();
		formPagador.ignorarNome();
		formPagador.ignorarTelefone();
	}

}
