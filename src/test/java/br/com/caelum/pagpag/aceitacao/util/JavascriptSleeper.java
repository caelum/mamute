package br.com.caelum.pagpag.aceitacao.util;

import org.openqa.selenium.WebDriver;

public class JavascriptSleeper {
	private final int TIMEOUT = 30000;
	private final int TENTATIVAS = 10;

	private final WebDriver driver;
	
	public JavascriptSleeper(WebDriver driver) {
		this.driver = driver;
	}

	public void esperarPaginaMudar() {
		String atual = driver.getPageSource();
		
		for (int i = 0; i < TENTATIVAS; i++) {
			
			if (!driver.getPageSource().equals(atual))
				return;
			
			
			try {
				Thread.sleep(TIMEOUT / TENTATIVAS);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		
		throw new RuntimeException("Estourou o timeout para redirecionamento");
	}
}