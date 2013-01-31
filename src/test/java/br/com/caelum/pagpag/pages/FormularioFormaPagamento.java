package br.com.caelum.pagpag.pages;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Select;

import br.com.caelum.pagpag.modelo.dominio.FormaPagamento;

public class FormularioFormaPagamento extends PageObject {
	private String parcelas = "1";
	private String numero = "4532561815124136";
	private String validade = "08/17";
	private String codigo = "123";
	private String nomePortador = "Zezinho Silva";
	private String nascimentoPortador = "22/03/1993";
	private String dddPortador = "21";
	private String telefonePortador = "33333333";
	private String cpfTitular = "368.465.413-22";
	private FormaPagamento forma = FormaPagamento.VISA;

	public FormularioFormaPagamento(WebDriver driver) {
		super(driver);
	}

	public ProcessamentoDePagamento preencheESubmita() {
		WebElement form = getForm();

		WebElement radioForma = driver.findElement(By.xpath(".//input[@value='"
				+ forma.name() + "']"));
		radioForma.click();

		if (forma.isCartaoCredito()) {
			Select selectParcelas = findParcelasSelect();
			WebElement inputCodigo = findCodigoInput();
			WebElement inputNomePortador = findNomePortadorInput();
			WebElement inputNascimentoPortador = findNascimentoPortadorInput();
			WebElement inputTelefonePortadorDDD = findTelefonePortadorDDDInput();
			WebElement inputTelefonePortador = findTelefonePortadorInput();
			WebElement inputCpfTitular = findCpfTitularInput();

			selecionar(selectParcelas, parcelas);
			preencherNumeroDoCartao();
			preencherValidadeDoCartao();
			preencher(inputCodigo, codigo);
			preencher(inputNomePortador, nomePortador);
			preencher(inputNascimentoPortador, nascimentoPortador, true);
			preencher(inputTelefonePortadorDDD, dddPortador, true);
			preencher(inputTelefonePortador, telefonePortador, true);
			preencher(inputCpfTitular, cpfTitular, true);
		}

		form.submit();
		
		return new ProcessamentoDePagamento(driver);
	}

	private WebElement getForm() {
		if (forma.isCartaoCredito()) {
			WebElement radioCartao = driver.findElement(By.id("cartao"));
			radioCartao.click();
			return driver.findElement(By.id("formCartao"));
		} else if (forma.isDebito()) {
			WebElement radioDebito = driver.findElement(By.id("debito"));
			radioDebito.click();
			return driver.findElement(By.id("formDebito"));
		}
		WebElement radioBoleto = driver.findElement(By.id("boleto"));
		radioBoleto.click();
		return driver.findElement(By.id("formBoleto"));
	}

	public WebElement findCpfTitularInput() {
		return byName("cartao.cpfTitular");
	}

	public WebElement findTelefonePortadorDDDInput() {
		return byId("cartao-telefonePortador-ddd");
	}

	public WebElement findTelefonePortadorInput() {
		return byId("cartao-telefonePortador-telefone");
	}

	public WebElement findNascimentoPortadorInput() {
		return byName("cartao.nascimentoPortador");
	}

	public WebElement findNomePortadorInput() {
		return byName("cartao.nomePortador");
	}

	public WebElement findCodigoInput() {
		return byName("cartao.codigo");
	}

	public WebElement findValidadeInput() {
		return byName("cartao.validade");
	}

	public Select findParcelasSelect() {
		return new Select(byId("cartao-parcelas-base").findElement(By.tagName("select")));
	}

	public FormaPagamento getForma() {
		return forma;
	}

	public FormularioFormaPagamento setForma(FormaPagamento formaPagamento) {
		this.forma = formaPagamento;
		return this;
	}

	public void setParcelas(String parcelas) {
		this.parcelas = parcelas;
	}

	public void setNumero(String numero) {
		this.numero = numero;
	}

	public void setValidade(String validade) {
		this.validade = validade;
	}

	public void setCodigo(String codigo) {
		this.codigo = codigo;
	}

	public void setNomePortador(String nomePortador) {
		this.nomePortador = nomePortador;
	}

	public void setNascimentoPortador(String nascimentoPortador) {
		this.nascimentoPortador = nascimentoPortador;
	}

	public void setDddPortador(String dddPortador) {
		this.dddPortador = dddPortador;
	}

	public void setTelefonePortador(String telefonePortador) {
		this.telefonePortador = telefonePortador;
	}

	public void setCpfTitular(String cpfTitular) {
		this.cpfTitular = cpfTitular;
	}

	public void ignorarParcelas() {
		setParcelas(null);
	}

	public void ignorarNumero() {
		setNumero(null);
	}

	public void ignorarValidade() {
		setValidade(null);
	}

	public void ignorarCodigo() {
		setCodigo(null);
	}

	public void ignorarNomePortador() {
		setNomePortador(null);
	}

	public void ignorarNascimentoPortador() {
		setNascimentoPortador(null);
	}

	public void ignorarDDDPortador() {
		setDddPortador(null);
	}

	public void ignorarTelefonePortador() {
		setTelefonePortador(null);
	}

	public void ignorarCpfTitular() {
		setCpfTitular(null);
	}

	private void preencherNumeroDoCartao() {
		if (numero == null) {
			return;
		}

		List<WebElement> inputs = driver.findElements(By
				.className("cartao-numero"));
		if (forma == FormaPagamento.VISA || forma == FormaPagamento.MASTER) {
			for (WebElement input : inputs) {
				String quatroDigitos = numero.substring(0, 4);
				input.sendKeys(quatroDigitos);
				numero = numero.substring(4);
			}
		} else if (forma == FormaPagamento.AMEEXP) {
			inputs.get(0).sendKeys(numero.substring(0, 4));
			inputs.get(1).sendKeys(numero.substring(4, 10));
			inputs.get(2).sendKeys(numero.substring(10));
		} else if (forma == FormaPagamento.DINERS
				|| forma == FormaPagamento.HIPERC) {
			inputs.get(0).sendKeys(numero);
		} else {
			throw new RuntimeException(
					"não foi implementado preenchimento do numero do cartao para a forma de pagamento "
							+ forma.getNome());
		}
	}

	private void preencherValidadeDoCartao() {
		if (validade == null) {
			return;
		}

		Select selectMes = new Select(driver.findElement(By
				.className("cartao-vencimento-mes")));
		Select selectAno = new Select(driver.findElement(By
				.className("cartao-vencimento-ano")));

		String[] splited = validade.split("/");
		String mes = splited[0];
		String ano = "20" + splited[1]; // PagPag não vai sobreviver 88 anos ;)

		selectAno.selectByValue(ano);
		selectMes.selectByValue(mes);
	}

	public int getParcelas() {
		return Integer.parseInt(parcelas);
	}

	public boolean validacoesDoPagadorEstaoVisiveis() {
		List<WebElement> erros = driver.findElement(By.id("infoCartao"))
				.findElements(By.className("error"));
		return !erros.isEmpty();
	}

	public int planoSelecionado() {
		String texto = driver.findElement(By.className("selecionado"))
				.getText();
		if (texto.contains("1 ano"))
			return 12;
		if (texto.contains("6 meses"))
			return 6;
		if (texto.contains("2 anos"))
			return 24;
		return 0;
	}

	public void mudaAssinatura(int plano) {
		driver.findElement(By.id("assinatura_" + plano)).click();
	}

}
