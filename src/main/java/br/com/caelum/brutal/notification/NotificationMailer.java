package br.com.caelum.brutal.notification;

import java.util.Locale;

import org.apache.commons.mail.Email;
import org.apache.commons.mail.EmailException;
import org.apache.log4j.Logger;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.owasp.html.HtmlPolicyBuilder;
import org.owasp.html.PolicyFactory;

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
    private static final PolicyFactory POLICY = new HtmlPolicyBuilder().allowElements("p", "br").toFactory();

    public NotificationMailer(Mailer mailer, TemplateMailer templates, Localization localization, Linker linker) {
        this.mailer = mailer;
        this.templates = templates;
        this.localization = localization;
        this.linker = linker;
    }

	public void send(NotificationMail notificationMail){
		DateTimeFormatter dateFormat = DateTimeFormat.forPattern("MMM, dd").withLocale(new Locale("pt", "br"));
		User to = notificationMail.getTo();
		Email email = templates.template(notificationMail.getEmailTemplate())
				.with("emailAction", notificationMail.getAction())
				.with("dateFormat", dateFormat)
				.with("sanitizer", POLICY)
				.with("localization", localization)
				.with("linkerHelper", new LinkToHelper(linker))
				.to(to.getName(), to.getEmail());
		email.setCharset("utf-8");
		try {
			mailer.send(email);
		} catch (EmailException e) {
			LOG.error("Could not send notifications mail to: " + to.getEmail());
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
