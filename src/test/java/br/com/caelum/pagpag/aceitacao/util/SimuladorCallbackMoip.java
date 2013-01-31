package br.com.caelum.pagpag.aceitacao.util;

import java.io.IOException;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.methods.PostMethod;

public class SimuladorCallbackMoip {

	private final HttpClient client;
	private final ServerInfo server;

	public SimuladorCallbackMoip(HttpClient client, ServerInfo server) {
		this.client = client;
		this.server = server;
	}

	public int enviarRequisicaoSimulada(String idProprio) {

		String url = server.urlFor("moip/tptfjrvfobebtfjnbttftpvcfttfejsjbqpsrvftfj/nasp_callback");
		PostMethod post = new PostMethod(url);
		post.addParameter("status_pagamento", "4");
		post.addParameter("id_transacao", idProprio);

		// informações irrelevantes por enquanto
		post.addParameter("cod_moip", "");
		post.addParameter("valor", "100");
		post.addParameter("forma_pagamento", "3");
		post.addParameter("tipo_pagamento", "CartaoDeCredito");
		post.addParameter("parcelas", "1");
		post.addParameter("email_consumidor", "zezinho@gmail.com");
		post.addParameter("cartao_bin", "123456");
		post.addParameter("cartao_final", "4321");
		post.addParameter("cartao_bandeira", "Visa");
		post.addParameter("cofre", "");
		
		int code = 0;
		try {
			code = client.executeMethod(post);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return code;
	}
	

}
