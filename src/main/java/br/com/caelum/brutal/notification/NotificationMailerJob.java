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

import br.com.caelum.brutal.dao.AnswerDAO;
import br.com.caelum.brutal.dao.CommentDAO;
import br.com.caelum.brutal.model.Subscribable;
import br.com.caelum.brutal.model.SubscribableAndUser;
import br.com.caelum.brutal.model.User;
import br.com.caelum.vraptor.Resource;
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

    public NotificationMailerJob(AnswerDAO answers, CommentDAO comments, Mailer mailer, TemplateMailer templates) {
        this.answers = answers;
        this.comments = comments;
        this.mailer = mailer;
        this.templates = templates;
    }

    @Override
    public void execute() {
        int hoursAgo = 3;
        Long milisecAgo = (long) (hoursAgo * (60 * 60 * 1000));
        DateTime threeHoursAgo = new DateTime(System.currentTimeMillis() - milisecAgo);
        List<SubscribableAndUser> recentSubscribables = answers.getSubscribablesAfter(threeHoursAgo);
        recentSubscribables.addAll(comments.getSubscribablesAfter(threeHoursAgo));
        
        Map<String, List<Subscribable>> subscribablesByEmail = new HashMap<>(); 
        Map<String, String> usersNames = new HashMap<>(); 
        for (SubscribableAndUser subscribableAndUser : recentSubscribables) {
            User user = subscribableAndUser.getUser();
            String userEmail = user.getEmail();
            List<Subscribable> subscribables = subscribablesByEmail.get(userEmail);
            if (subscribableAndUser != null) {
                subscribables = new ArrayList<>();
            }
            subscribables.add(subscribableAndUser.getSubscribable());
            subscribablesByEmail.put(userEmail, subscribables);
            usersNames.put(userEmail, user.getName());
        }
        
        for (Entry<String, List<Subscribable>> entry : subscribablesByEmail.entrySet()) {
            String userEmail = entry.getKey();
            DateTimeFormatter dateFormat = DateTimeFormat.forPattern("MMM, dd").withLocale(new Locale("pt", "br"));
            
            Email email = templates.template("notifications_mail")
                    .with("subscribables", entry.getValue())
                    .with("dateFormat", dateFormat)
                    .to(usersNames.get(userEmail), userEmail);
            try {
                mailer.send(email);
            } catch (EmailException e) {
                LOG.error("Could not send notifications mail to: " + userEmail);
            }
        }
    }

    @Override
    public String frequency() {
        //return "0 0 0/3 * * ?";
        return "0 0/1 * * * ?";
    }

}
