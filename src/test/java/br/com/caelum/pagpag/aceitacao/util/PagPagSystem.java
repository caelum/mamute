package br.com.caelum.pagpag.aceitacao.util;

import static org.junit.Assert.assertEquals;

import java.io.File;
import java.io.IOException;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.methods.FileRequestEntity;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.RequestEntity;
import org.openqa.selenium.WebDriver;

import br.com.caelum.pagpag.pages.FormularioDadosPagador;
import br.com.caelum.pagpag.test.utils.RecursoTeste;

public class PagPagSystem implements ServerInfo.TesteAceitacao {

	private final WebDriver driver;

	public PagPagSystem(WebDriver driver) {
		this.driver = driver;
	}

	public FormularioDadosPagador postComXMLDoPagamento(RecursoTeste recurso) {
		String xmlPath = recurso.path();
		File xml = new File(xmlPath);

		String url = SERVER.urlFor("pagamento/");
		PostMethod post = new PostMethod(url);
		RequestEntity entity = new FileRequestEntity(xml, "application/xml");
		post.setRequestEntity(entity);

		HttpClient client = new HttpClient();
		try {
			int code = client.executeMethod(post);
			assertEquals("1o: POST com XML foi bem sucedido e criado", 201, code);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
		String uri = post.getResponseHeader("Location").getValue();
		driver.get(uri);
		return new FormularioDadosPagador(driver);
	}
}
