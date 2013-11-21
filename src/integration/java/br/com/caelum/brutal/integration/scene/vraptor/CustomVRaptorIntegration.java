package br.com.caelum.brutal.integration.scene.vraptor;

import java.io.IOException;

import br.com.caelum.brutal.util.DataImport;
import br.com.caelum.vraptor.environment.ServletBasedEnvironment;
import br.com.caelum.vraptor.test.VRaptorIntegration;
import br.com.caelum.vraptor.test.http.Parameters;
import br.com.caelum.vraptor.test.requestflow.UserFlow;

public class CustomVRaptorIntegration extends VRaptorIntegration {

	{
		System.setProperty(ServletBasedEnvironment.ENVIRONMENT_PROPERTY, "acceptance");
//		importData();
	}

	private void importData() {
		try {
			new DataImport().run();
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	protected UserFlow login(UserFlow navigation, String email, String password) {
		return navigation.post("/login", Parameters.initWith("email", email).add("password", password));
		
	}

	protected UserFlow createQuestion(UserFlow navigation, String title, String description,
			String tagNames, boolean watching) {
				return navigation.post("/perguntar", Parameters.initWith("title", title).add("description", description).add("tagNames", tagNames).add("watching", watching));
			}
	
	
	
}
