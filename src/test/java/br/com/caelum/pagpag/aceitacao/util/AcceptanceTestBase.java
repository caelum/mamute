package br.com.caelum.pagpag.aceitacao.util;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertSame;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchWindowException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriver.Navigation;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.WebDriverWait;

import br.com.caelum.pagpag.modelo.XStreamFactory;
import br.com.caelum.pagpag.modelo.dominio.Carrinho;
import br.com.caelum.pagpag.modelo.dominio.MudancaStatus;
import br.com.caelum.pagpag.modelo.dominio.Pagamento;
import br.com.caelum.pagpag.modelo.dominio.Parcelamento;
import br.com.caelum.pagpag.modelo.dominio.StatusPagamento;
import br.com.caelum.pagpag.modelo.moip.ServicoMoip;

import com.thoughtworks.xstream.XStream;

public abstract class AcceptanceTestBase implements ServerInfo.TesteAceitacao {

	protected static WebDriver driver;
	private static List<String> janelasAFechar;

	@AfterClass
	public static void fechaPopups() {
		for (String window : janelasAFechar) {
			try {
				driver.switchTo().window(window).close();
			} catch (NoSuchWindowException e) {
				// ignoring when unable to close the window
			}
		}
	}

	@BeforeClass
	public static void inicializaLista() {
		janelasAFechar = new ArrayList<String>();
	}

	protected HttpClient client;

	protected JavascriptSleeper jsSleeper;

	protected SimuladorCallbackMoip simuladorCallbackMoip;

	private String windowPrincipal;

	protected ServicoMoip buscaServico(String uuid) {
		String path = "admin/urlbizarra/servico/" + uuid;
		String xml = getFor(path);
		
		XStream xstream = new XStreamFactory().factory();
		xstream.processAnnotations(ServicoMoip.class);
		xstream.processAnnotations(Pagamento.class);
		xstream.processAnnotations(Carrinho.class);
		xstream.processAnnotations(MudancaStatus.class);
		xstream.processAnnotations(Parcelamento.class);
		return (ServicoMoip) xstream.fromXML(xml.replaceAll("class=.persistentBag.",""));
	}

	public String getFor(String path) {
		String uri = uriFor(path);
		GetMethod get = new GetMethod(uri);
		try {
			new HttpClient().executeMethod(get);
			return get.getResponseBodyAsString();
		} catch (IOException e) {
			e.printStackTrace();
			return "";
		}
	}

	private String uriFor(String path) {
		String currentUrl = driver.getCurrentUrl();
		String urlBase = currentUrl.replaceAll("formapagamento.*$", "");
		return urlBase + path;
	}

	@After
	public void fecha() {
		for (String window : driver.getWindowHandles()) {
			if (!window.equals(windowPrincipal)) {
				janelasAFechar.add(window);
			}
		}

		if (driver != null)
			driver.close();
	}

	private WebDriver htmlUnitDriver() {
		HtmlUnitDriver driver = new HtmlUnitDriver();
		driver.setJavascriptEnabled(true);
		return driver;
	}

	protected Navigation browser() {
		return driver.navigate();
	}
	
	protected PagPagSystem pagpag() {
		return new PagPagSystem(driver);
	}

	@Before
	public void setUpEnv() {
		getDriver();

		driver.manage().timeouts().implicitlyWait(25, TimeUnit.SECONDS);
		waitForFirstBodyPresence();
		jsSleeper = new JavascriptSleeper(driver);
		client = new HttpClient();
		simuladorCallbackMoip = new SimuladorCallbackMoip(client, SERVER);

		windowPrincipal = driver.getWindowHandle();
	}

	private void getDriver() {
		if ("htmlunit".equals(System.getProperty("vraptor.browser"))) {
			driver = htmlUnitDriver();
		} else {
			driver = new FirefoxDriver();
		}
	}

	protected void simulaCallbackDoMoip(ServicoMoip servico,
			int numeroDeParcelas) {

		String url = SERVER
				.urlFor("moip/tptfjrvfobebtfjnbttftpvcfttfejsjbqpsrvftfj/nasp_callback");
		PostMethod post = new PostMethod(url);
		post.addParameter("status_pagamento", "4");
		post.addParameter("id_transacao", servico.getIdProprio());

		// informações irrelevantes por enquanto
		post.addParameter("cod_moip", "");
		post.addParameter("valor", "100");
		post.addParameter("forma_pagamento", "3");
		post.addParameter("tipo_pagamento", "CartaoDeCredito");
		post.addParameter("parcelas", "" + numeroDeParcelas);
		post.addParameter("email_consumidor", "zezinho@gmail.com");
		post.addParameter("cartao_bin", "123456");
		post.addParameter("cartao_final", "4321");
		post.addParameter("cartao_bandeira", "Visa");
		post.addParameter("cofre", "");

		try {
			int code = client.executeMethod(post);
			assertEquals("Postar a noticia de callback com situação 4", 200,
					code);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	protected void verificaOQueFoiGravadoNoBanco(ServicoMoip servico,
			int mudancasEsperadas, int parcelasEsperadas) {
		verificaOQueFoiGravadoNoBanco(servico, mudancasEsperadas,
				parcelasEsperadas, StatusPagamento.CONCLUIDO);
	}

	protected void verificaOQueFoiGravadoNoBanco(ServicoMoip servico,
			int mudancasEsperadas, int parcelasEsperadas,
			StatusPagamento statusEsperado) {

		servico = buscaServico(servico.getUuid());

		assertNotNull(servico);
		assertNotNull(servico.getPagamento());
		assertNotNull(servico.getPagamento().getDataCriacao());
		assertNotNull(servico.getPagamento().getRepresentante());
		assertNotNull(servico.getPagamento().getRepresentante().getEndereco());
		assertNotNull(servico.getPagamento().getForma());
		assertNotNull(servico.getToken());
		assertNotNull(servico.getIdMoip());
		assertSame(statusEsperado, servico.getPagamento().getStatus());
		if (statusEsperado.equals(StatusPagamento.CONCLUIDO)) {
			assertNotNull(servico.getPagamento().getDataConclusao());
		}

		List<Parcelamento> parcelamentos = servico.getPagamento().getCarrinho()
				.getParcelamentos();
		List<MudancaStatus> mudancas = servico.getPagamento()
				.getMudancasStatus();

		assertEquals(mudancasEsperadas, mudancas.size());
		assertEquals(parcelasEsperadas, parcelamentos.size());
	}
	

	private void waitForFirstBodyPresence() {
		driver.get(SERVER.urlFor("teste/pagamento/"));
		ExpectedCondition<WebElement> homeAppear = new ExpectedCondition<WebElement>() {
			@Override
			public WebElement apply(WebDriver d) {
				return d.findElement(By.name("tipoXML"));
			}
		};
		new WebDriverWait(driver, 40).until(homeAppear);
	}

}
