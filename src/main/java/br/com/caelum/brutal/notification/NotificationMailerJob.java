package br.com.caelum.brutal.notification;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.mail.Email;
import org.apache.commons.mail.EmailException;
import org.apache.log4j.Logger;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import br.com.caelum.brutal.controllers.QuestionController;
import br.com.caelum.brutal.dao.AnswerDAO;
import br.com.caelum.brutal.dao.CommentDAO;
import br.com.caelum.brutal.model.Question;
import br.com.caelum.brutal.model.SubscribableDTO;
import br.com.caelum.brutal.model.User;
import br.com.caelum.brutal.vraptor.Linker;
import br.com.caelum.vraptor.Resource;
import br.com.caelum.vraptor.core.Localization;
import br.com.caelum.vraptor.quartzjob.CronTask;
import br.com.caelum.vraptor.simplemail.Mailer;
import br.com.caelum.vraptor.simplemail.template.TemplateMailer;

@Resource
public class NotificationMailerJob implements CronTask {
    
    private final AnswerDAO answers;
    private final CommentDAO comments;
    private final Mailer mailer;
    private final TemplateMailer templates;
    private static final Logger LOG = Logger.getLogger(NotificationMailerJob.class);
    private final Localization localization;
    private final Linker linker; 

    public NotificationMailerJob(AnswerDAO answers, CommentDAO comments, Mailer mailer,
            TemplateMailer templates, Localization localization, Linker linker) {
        this.answers = answers;
        this.comments = comments;
        this.mailer = mailer;
        this.templates = templates;
        this.localization = localization;
        this.linker = linker;
    }

    @Override
    public void execute() {
        DateTime threeHoursAgo = new DateTime().minusHours(3);
        
        List<SubscribableDTO> recentSubscribables = answers.getSubscribablesAfter(threeHoursAgo);
        recentSubscribables.addAll(comments.getSubscribablesAfter(threeHoursAgo));
        
        Map<String, List<SubscribableDTO>> subscribablesByEmail = groupByUserEmail(recentSubscribables);
        sendEmails(subscribablesByEmail);
    }

    private Map<String, List<SubscribableDTO>> groupByUserEmail(List<SubscribableDTO> recentSubscribables) {
        Map<String, List<SubscribableDTO>> subscribablesByEmail = new HashMap<>(); 
        for (SubscribableDTO subscribableDTO : recentSubscribables) {
            User user = subscribableDTO.getUser();
            String userEmail = user.getEmail();
            List<SubscribableDTO> subscribables = subscribablesByEmail.get(userEmail);
            if (subscribableDTO != null) {
                subscribables = new ArrayList<>();
            }
            subscribables.add(subscribableDTO);
            subscribablesByEmail.put(userEmail, subscribables);
        }
        return subscribablesByEmail;
    }

    private void sendEmails(Map<String, List<SubscribableDTO>> subscribablesByEmail) {
        for (Entry<String, List<SubscribableDTO>> entry : subscribablesByEmail.entrySet()) {
            User user = entry.getValue().get(0).getUser();
            DateTimeFormatter dateFormat = DateTimeFormat.forPattern("MMM, dd").withLocale(new Locale("pt", "br"));
            
            Email email = templates.template("notifications_mail")
                    .with("subscribablesDTO", entry.getValue())
                    .with("dateFormat", dateFormat)
                    .with("localization", localization)
                    .with("linkerHelper", new LinkToHelper(linker))
                    .to(user.getName(), user.getEmail());
            try {
                mailer.send(email);
            } catch (EmailException e) {
                LOG.error("Could not send notifications mail to: " + user.getEmail());
            }
        }
    }

    @Override
    public String frequency() {
        return "0 0 0/3 * * ?";
    }
    
    public class LinkToHelper {
        private Linker linker;

        public LinkToHelper(Linker linker) {
            this.linker = linker;
        }
        
        public String questionLink(Question q) {
            linker.linkTo(QuestionController.class).showQuestion(q.getId(), q.getSluggedTitle());
            return linker.get();
        }
        
    }

}
