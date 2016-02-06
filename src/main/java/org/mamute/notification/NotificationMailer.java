package org.mamute.notification;

import static org.joda.time.format.DateTimeFormat.forPattern;

import java.lang.reflect.Method;
import java.util.Locale;

import javax.inject.Inject;

import net.vidageek.mirror.dsl.Mirror;

import org.apache.commons.mail.Email;
import org.apache.commons.mail.EmailException;
import org.apache.log4j.Logger;
import org.joda.time.format.DateTimeFormatter;
import org.mamute.controllers.ListController;
import org.mamute.controllers.NewsController;
import org.mamute.controllers.QuestionController;
import org.mamute.controllers.UserProfileController;
import org.mamute.mail.action.EmailAction;
import org.mamute.model.News;
import org.mamute.model.Question;
import org.mamute.model.Tag;
import org.mamute.model.User;
import org.mamute.model.interfaces.Watchable;
import org.mamute.vraptor.Env;
import org.owasp.html.HtmlPolicyBuilder;
import org.owasp.html.PolicyFactory;

import br.com.caelum.vraptor.environment.Environment;
import br.com.caelum.vraptor.http.route.Router;
import br.com.caelum.vraptor.simplemail.Mailer;
import br.com.caelum.vraptor.simplemail.template.BundleFormatter;
import br.com.caelum.vraptor.simplemail.template.TemplateMailer;

public class NotificationMailer {
	private static final Logger LOG = Logger.getLogger(NotificationMailer.class);
	private static final PolicyFactory POLICY = new HtmlPolicyBuilder().toFactory();
    
    @Inject private Mailer mailer;
    @Inject private TemplateMailer templates;
    @Inject private Locale locale;
	@Inject private Environment env;
	@Inject private Env brutalEnv;
	@Inject private Router router;
	@Inject private BundleFormatter bundle;

	public void send(NotificationMail notificationMail) {
		User to = notificationMail.getTo();
		Email email = buildEmail(notificationMail);
		email.setCharset("utf-8");
		try {
			mailer.send(email);
		} catch (EmailException e) {
			LOG.error("Could not send notifications mail to: " + to.getEmail(), e);
		}
	}

	public Email buildEmail(NotificationMail notificationMail) {
		DateTimeFormatter dateFormat = forPattern("MMM, dd").withLocale(new Locale("pt", "br"));
		EmailAction action = notificationMail.getAction();
		User to = notificationMail.getTo();
		Email email = templates.template(notificationMail.getEmailTemplate(), bundle.getMessage("site.name"), action.getMainThread().getTitle())
				.with("emailAction", action)
				.with("dateFormat", dateFormat)
				.with("sanitizer", POLICY)
				.with("bundle", bundle)
				.with("watcher", to)
				.with("linkerHelper", new LinkToHelper(router, brutalEnv))
				.with("logoUrl", env.get("mail_logo_url"))
				.to(to.getName(), to.getEmail());
		return email;
	}
    
	public static class LinkToHelper {

        private final Router router;
		private final Env env;
		
        public LinkToHelper(Router router, Env env) {
			this.router = router;
			this.env = env;
        }
        
        public String mainThreadLink(Watchable watchable) {
			if(watchable.getType().isAssignableFrom(News.class)) {
				News news = (News) watchable;
        		return urlFor(NewsController.class, "showNews", news, news.getSluggedTitle());
			}else{
				Question question = (Question) watchable;
				return urlFor(QuestionController.class, "showQuestion", question, question.getSluggedTitle());
        	}
        }
        
		public String tagLink(Tag t) {
			return urlFor(ListController.class, "withTag", t.getName(),1 , false);
        }
        
		public String newsLink(News n) {
        	return urlFor(NewsController.class, "showNews", n, n.getSluggedTitle());
        }

        public String questionLink(Question q) {
        	return urlFor(QuestionController.class, "showQuestion", q, q.getSluggedTitle());
        }
        
        public String userLink(User u) {
        	return urlFor(UserProfileController.class, "showProfile", u, u.getSluggedName());
        }
        
        public String unsubscribeLink(User user) {
        	return urlFor(UserProfileController.class, "unsubscribe", user, user.getUnsubscribeHash());
        }


		private String urlFor(Class<?> clazz, String method,
				Object...args) {
			String relativePath = router.urlFor(clazz, method(clazz, method), args);
			return env.getHostAndContext() + relativePath;
		}

		private Method method(Class<?> clazz, String method) {
			return new Mirror().on(clazz).reflect().method(method).withAnyArgs();
		}
    }

}
