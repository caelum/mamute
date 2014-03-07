package org.mamute.infra;

import java.util.Random;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.Observes;
import javax.inject.Inject;

import org.apache.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.mamute.model.LoginMethod;
import org.mamute.model.User;

import br.com.caelum.vraptor.events.VRaptorInitialized;

@ApplicationScoped
public class DefaultAdminCreator {
	
	@Inject private SessionFactory sessionFactory;
	
	private static Logger LOG = Logger.getLogger(DefaultAdminCreator.class);
	
	public void createDefaultAdmin(@Observes VRaptorInitialized initialized) {
		Session session = sessionFactory.openSession();
		session.beginTransaction();
		Long result = (Long) session.createQuery("select count(*) from User").uniqueResult();
		
		if (result == 0) {
			Random random = new Random(System.currentTimeMillis());
			String email = "moderator@mamuteqa.org";
			User newUser = new User("moderator", email).asModerator();
			
			String number = Long.toString(random.nextLong());
			String password = Digester.md5(number);
			
			LoginMethod brutalLogin = LoginMethod.brutalLogin(newUser, email, password);
			newUser.add(brutalLogin);
			session.save(brutalLogin);
			session.save(newUser);
			
			LOG.info("=========================");
			LOG.info("New admin user created!");
			LOG.info("email: " + email);
			LOG.info("password: " + password);
			LOG.info("=========================");
		}
		
		session.getTransaction().commit();
	}
}
