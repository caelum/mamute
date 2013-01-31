package br.com.caelum.pagpag.test.utils;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import org.apache.commons.io.IOUtils;

public enum RecursoTeste {

	XML_INICIAR_PAGAMENTO("/xml/iniciarProcessoDePagamento.xml"),
	XML_ASSINATURA("/xml/assinatura.xml"),
	XML_INICIAR_PAGAMENTO_SEM_REPRESENTANTE("/xml/iniciarProcessoDePagamentoSemRepresentante.xml"),
	XML_INICIAR_PAGAMENTO_REPRESENTANTE_SO_COM_CPF("/xml/iniciarProcessoDePagamento_RepresentanteSoComCpf.xml"),
	XML_INICIAR_PAGAMENTO_PARCELADO("/xml/iniciarProcessoDePagamentoParcelado.xml"),
	XML_MOIP_INSTRUCAO_UNICA("/xml/moip/instrucaoUnica.xml"),
	XML_MOIP_INSTRUCAO_UNICA_COM_PARCELAMENTOS("/xml/moip/instrucaoUnicaComParcelamentos.xml"),
	XML_MOIP_RESPOSTA("/xml/moip/resposta.xml"),
	XML_NOTIFICACAO_MUDANCA_STATUS("/xml/notificacaoMudancaStatus.xml");
	
	private String path;

	RecursoTeste(String relativePath) {
		this.path = this.getClass().getResource(relativePath)
				.getPath();
	}

	public String ler() {
		File file = new File(path);
		
		try {
			FileReader reader = new FileReader(file);
			return IOUtils.toString(reader);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
		
	}

	public String path() {
		return this.path;
	}
}
