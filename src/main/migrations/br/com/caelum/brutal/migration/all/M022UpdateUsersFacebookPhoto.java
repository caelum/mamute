package br.com.caelum.brutal.migration.all;

import static java.util.Arrays.asList;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.StatelessSession;
import org.scribe.model.Token;
import org.scribe.oauth.OAuthService;

import br.com.caelum.brutal.auth.FacebookApi;
import br.com.caelum.brutal.auth.OAuthServiceCreator;
import br.com.caelum.brutal.migration.Migration;
import br.com.caelum.brutal.migration.MigrationOperation;
import br.com.caelum.brutal.migration.RawSQLOperation;
import br.com.caelum.brutal.model.LoginMethod;
import br.com.caelum.brutal.model.MethodType;
import br.com.caelum.brutal.model.User;
import br.com.caelum.vraptor.environment.DefaultEnvironment;
import br.com.caelum.vraptor.environment.Environment;
import br.com.caelum.vraptor.ioc.ApplicationScoped;
import br.com.caelum.vraptor.ioc.Component;

import com.google.common.base.Predicate;
import com.google.common.collect.Iterables;

@Component
@ApplicationScoped
public class M022UpdateUsersFacebookPhoto implements Migration {

	@Override
	public List<MigrationOperation> up() {
		MigrationOperation operation = new MigrationOperation() {
			@Override
			@SuppressWarnings("unchecked")
			public void execute(Session session, StatelessSession statelessSession) {
				Environment env = getEnv();
				OAuthService oauthService = buildFacebookService(env);
				
				String hql = "select u from User u join fetch u.loginMethods method where method.type=:type";
				Query query = session.createQuery(hql);
				List<User> users = query.setParameter("type", MethodType.FACEBOOK).list();
				for (User user : users) {
					Iterable<LoginMethod> facebookMethods = Iterables.filter(user.getLoginMethods(), new Predicate<LoginMethod>() {
						public boolean apply(LoginMethod method) {
							return method.isFacebook();
						}
					});
					for (LoginMethod facebookMethod : facebookMethods) {
						try {
							String token = facebookMethod.getToken();
							FacebookApi facebookApi = new FacebookApi(oauthService, new Token(token, ""));
							String userName = facebookApi.getUserName();
							String photoUri = "http://graph.facebook.com/"+userName+"/picture";
							setPhotoUri(user, photoUri);
						} catch (IllegalArgumentException e) {
							System.out.println("could not update photo of " + user);
						}
					}
				}
			}

		};
		return asList(operation);
	}

	@Override
	public List<MigrationOperation> down() {
		return RawSQLOperation.forSqls("");
	}

	private Environment getEnv() {
		Environment env;
		try {
			env = new DefaultEnvironment("production");
		} catch (IOException e) {
			throw new RuntimeException();
		}
		return env;
	}
	
	private OAuthService buildFacebookService(Environment env) {
		OAuthServiceCreator creator = new OAuthServiceCreator(env);
		creator.create();
		OAuthService service = creator.getInstance();  
		return service;
		
	}
	
	private void setPhotoUri(User user, String photoUri) {
		try {
			user.setPhotoUri(new URL(photoUri));
		} catch (MalformedURLException e) {
			throw new RuntimeException();
		}
	}
}