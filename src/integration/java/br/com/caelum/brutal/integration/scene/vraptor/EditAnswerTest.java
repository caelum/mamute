package br.com.caelum.brutal.integration.scene.vraptor;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;
import org.junit.AfterClass;
import org.junit.Test;

import br.com.caelum.vraptor.test.VRaptorIntegration;
import br.com.caelum.vraptor.test.VRaptorTestResult;
import br.com.caelum.vraptor.test.http.Parameters;

public class EditAnswerTest extends VRaptorIntegration {

	@Test
	public void should_edit_answer_of_other_author() throws Exception {
		VRaptorTestResult result = navigate()
		.post("/login",
				Parameters.initWith("email", "moderator@caelum.com.br")
				.add("password", "123456")
		).post("/perguntar",
				Parameters.initWith("title", "Titulo da questao hahaha")
				.add("description", "Descricao da questao longa demais")
				.add("tagNames", "java")
		).followRedirect().execute();
		System.out.println("** "+result.getResponseBody());
		System.out.println(result.getLastPath());
	}

	@Test
	public void simple() throws Exception {
		VRaptorTestResult result = navigate()
				.get("/").followRedirect().execute();
		System.out.println("** "+result.getResponseBody());
		System.out.println(result.getLastPath());
	}
	
	@AfterClass
	public static void tearDownClass() throws IOException {
		FileUtils.deleteDirectory(new File("jsp-compilation/"));
	}

}
