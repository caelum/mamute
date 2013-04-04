package br.com.caelum.brutal.notification;

import static org.apache.commons.lang.CharEncoding.UTF_8;

import java.util.List;
import java.util.Locale;

import org.apache.commons.mail.Email;
import org.apache.log4j.Logger;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import br.com.caelum.brutal.controllers.QuestionController;
import br.com.caelum.brutal.model.Question;
import br.com.caelum.brutal.model.User;
import br.com.caelum.brutal.vraptor.Linker;
import br.com.caelum.vraptor.core.Localization;
import br.com.caelum.vraptor.ioc.Component;
import br.com.caelum.vraptor.simplemail.Mailer;
import br.com.caelum.vraptor.simplemail.template.TemplateMailer;

@Component
public class NotificationMailer {
    
    private final Mailer mailer;
    private final TemplateMailer templates;
    private final Localization localization;
    private final Linker linker;
    private static final Logger LOG = Logger.getLogger(NotificationMailer.class);

    public NotificationMailer(Mailer mailer, TemplateMailer templates, Localization localization, Linker linker) {
        this.mailer = mailer;
        this.templates = templates;
        this.localization = localization;
        this.linker = linker;
    }

    public void sendEmails(List<SubscribableEmail> subscribablesEmails) {
    	for (SubscribableEmail subscribableEmail : subscribablesEmails) {
    		User user = subscribableEmail.getUser();
    		try {
    			DateTimeFormatter dateFormat = DateTimeFormat.forPattern("MMM, dd").withLocale(new Locale("pt", "br"));
    			
    			Email email = templates.template("notifications_mail")
    					.with("subscribablesDTO", subscribableEmail.getSubscribables())
    					.with("dateFormat", dateFormat)
    					.with("localization", localization)
    					.with("linkerHelper", new LinkToHelper(linker))
    					.to(user.getName(), user.getEmail());
    			email.setCharset(UTF_8);
    			mailer.send(email);
    		} catch (Exception e) {
    			LOG.error("Could not send notifications mail to: " + user.getEmail());
    		}
    	}
    }
    
    public class LinkToHelper {
        private Linker linker;

        public LinkToHelper(Linker linker) {
            this.linker = linker;
        }
        
        public String questionLink(Question q) {
            linker.linkTo(QuestionController.class).showQuestion(q, q.getSluggedTitle());
            return linker.get();
        }
        
    }


}
