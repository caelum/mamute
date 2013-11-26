package br.com.caelum.brutal.integration.scene.vraptor;

import java.io.IOException;

import org.hibernate.Session;

import br.com.caelum.brutal.dao.InvisibleForUsersRule;
import br.com.caelum.brutal.dao.QuestionDAO;
import br.com.caelum.brutal.dao.TagDAO;
import br.com.caelum.brutal.dao.UserDAO;
import br.com.caelum.brutal.model.LoggedUser;
import br.com.caelum.brutal.model.Tag;
import br.com.caelum.brutal.model.User;
import br.com.caelum.brutal.util.DataImport;
import br.com.caelum.vraptor.environment.ServletBasedEnvironment;
import br.com.caelum.vraptor.test.VRaptorIntegration;
import br.com.caelum.vraptor.test.http.Parameters;
import br.com.caelum.vraptor.test.requestflow.UserFlow;

public class CustomVRaptorIntegration extends VRaptorIntegration {

	private DataImport dataImport;

	{
		System.setProperty(ServletBasedEnvironment.ENVIRONMENT_PROPERTY,
				"acceptance");
		dataImport = new DataImport();
		importData();
		dataImport.getSession().beginTransaction();
	}

	private void importData() {
		try {
			dataImport.run();
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	protected UserFlow login(UserFlow navigation, String email, String password) {
		return navigation.post("/login", Parameters.initWith("email", email)
				.add("password", password));
	}

	protected UserFlow createQuestion(UserFlow navigation, String title,
			String description, String tagNames, boolean watching) {
		return navigation.post("/perguntar", 
				Parameters.initWith("title", title)
					.add("description", description)
					.add("tagNames", tagNames).add("watching", watching));
	}
	
	protected QuestionDAO questionDao() {
		InvisibleForUsersRule invisible = new InvisibleForUsersRule(new LoggedUser(null, null));
		return new QuestionDAO(dataImport.getSession(), invisible);
	}
	
	protected UserDAO userDao() {
		return new UserDAO(dataImport.getSession());
	}
	
	protected User moderator() {
		UserDAO users = new UserDAO(dataImport.getSession());
		return users.findByMailAndPassword("moderator@caelum.com.br", "123456");
	}
	
	protected Tag tag(String name) {
		return new TagDAO(this.dataImport.getSession()).findByName(name);
	}
	
	protected void commit() {
		Session session = this.dataImport.getSession();
		session.getTransaction().commit();
		session.beginTransaction();
	}
	
}
