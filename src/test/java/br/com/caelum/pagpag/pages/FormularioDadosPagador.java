package br.com.caelum.pagpag.pages;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Select;

public class FormularioDadosPagador extends PageObject {

	private String nome = "Zezinho Silva";
	private String email = "zezinho@gmail.com";
	private String cpf = "368.465.413-22";
	private String estado = "RJ";
	private String numeroTelefone= "33333333";
	private String ddd = "21";

	public FormularioDadosPagador(WebDriver driver) {
		super(driver);
	}

	public WebElement preenche() {
		Select selectEstado = findEstadoSelect();
		WebElement inputNome = findNomeInput();
		WebElement inputEmail = findEmailInput();
		WebElement inputCpf = findCpfInput();
		WebElement inputDDD = findDDDInput();
		WebElement inputNumeroTelefone = findNumeroTelefoneInput();

		preencher(inputNome, nome);
		preencher(inputEmail, email);
		preencher(inputCpf, cpf);
		preencher(inputDDD, ddd);
		preencher(inputNumeroTelefone, numeroTelefone);
		preencher(selectEstado, estado);

		inputNome.click();
		
		type("representanteFormBean.endereco.logradouro", "Rua Vergueiro");
		type("representanteFormBean.endereco.bairro", "Vila Mariana");
		type("representanteFormBean.endereco.cep", "04112901");
		type("representanteFormBean.endereco.cidade", "São Paulo");

		return inputNumeroTelefone;
	}

	public WebElement findNumeroTelefoneInput() {
		return byName("representanteFormBean.telefone.telefoneLocal");
	}

	public WebElement findDDDInput() {
		return byName("representanteFormBean.telefone.ddd");
	}

	public void type(String field, String value) {
		byName(field).sendKeys(value);
	}

	private WebElement findCpfInput() {
		return byName("representanteFormBean.cpf");
	}

	private WebElement findEmailInput() {
		return byName("representanteFormBean.email");
	}

	public WebElement findNomeInput() {
		return byName("representanteFormBean.nome");
	}

	private Select findEstadoSelect() {
		return new Select(byName("representanteFormBean.endereco.estado"));
	}

	public FormularioDadosPagador setNome(String nome) {
		this.nome = nome;
		return this;
	}

	public FormularioDadosPagador setEmail(String email) {
		this.email = email;
		return this;
	}

	public void setCpf(String cpf) {
		this.cpf = cpf;
	}

	public void setNumeroTelefone(String telefone) {
		this.numeroTelefone = telefone;
	}

	public void setDDD(String ddd) {
		this.ddd = ddd;
	}

	private void setEstado(String estado) {
		this.estado = estado;
	}

	public void ignorarNome() {
		setNome(null);
	}

	public void ignorarEmail() {
		setEmail(null);
	}

	public void ignorarCpf() {
		setCpf(null);
	}

	public void ignorarTelefone() {
		setNumeroTelefone(null);
		setDDD(null);
	}

	public void ignorarEstado() {
		setEstado(null);
	}

	public void submita() {
		WebElement confirma = byName("_method");
		confirma.click();
	}

	public void setTelefone(String telefone) {
		this.ddd = telefone.substring(1, 3);
		this.numeroTelefone = telefone.substring(4, telefone.length());
	}

	public String getEstado() {
		return findEstadoSelect().getFirstSelectedOption()
				.getAttribute("value");
	}

	public String getNome() {
		return getValue(findNomeInput());
	}

	private String getValue(WebElement elem) {
		return elem.getAttribute("value");
	}

	public String getEmail() {
		return getValue(findEmailInput());
	}

	public String getCpf() {
		return getValue(findCpfInput());
	}

	public String getTelefone() {
		return getValue(byName("representanteFormBean.telefone.ddd"))
				+ getValue(byName("representanteFormBean.telefone.telefoneLocal"));
	}

	public boolean validacoesEstaoVisiveis() {
		List<WebElement> erros = driver.findElement(
				By.className("informacoes-usuario")).findElements(
				By.name("error"));

		for (WebElement erro : erros) {
			if (!erro.isDisplayed())
				return false;
		}
		return true;
	}

	public FormularioFormaPagamento getAbaDeFormaDePagamento() {
		return new FormularioFormaPagamento(driver);
	}
}
