package br.com.caelum.pagpag.pages;

import static org.openqa.selenium.support.ui.ExpectedConditions.textToBePresentInElement;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;

public class ProcessamentoDePagamento extends PageObject{


	public ProcessamentoDePagamento(WebDriver driver) {
		super(driver);
	}

	private boolean esperaTelaAparecer() {
		return "http://www.google.com/".equals(byId("voltar-ao-sistema").getAttribute("href"));
	}

	private String retornaUUIDDoServico() {
		return byId("PagPagData").getAttribute("data-servicoUUID");
	}
	
	public String extraiUUID() {
		String uuid = retornaUUIDDoServico();
		esperaTelaAparecer();
		return uuid;
	}

	public boolean garanteLinkDeDebito() {
		return garanteLinkDo("banco");
	}

	private boolean garanteLinkDo(String conteudo) {
		WebDriverWait wait = new WebDriverWait(driver, 40);
		wait.until(textToBePresentInElement(By.id("abrir-pagamento-em-janela"), conteudo));

		String elemento = byId("abrir-pagamento-em-janela").getAttribute("href");
		return elemento.contains("moip");
	}

	public boolean garanteTelaDeBoleto() {
		return garanteLinkDo("clique aqui");
	}

	public boolean contemSucesso() {
		return driver.findElement(By.id("msg-final")).isDisplayed();
	}

	public boolean boletoContem(String text) {
		driver.switchTo().frame(driver.findElement(By.id("abrir-pagamento-em-iframe")));
		return driver.getPageSource().contains(text);
	}

	public String compraEfetuadaPossuiValor() {
		return driver.findElement(By.className("valor-da-compra")).getText();
	}

}
