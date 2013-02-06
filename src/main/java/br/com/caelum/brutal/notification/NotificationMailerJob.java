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
import br.com.caelum.brutal.model.SubscribableDTO;
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
        //return "0 0 0/3 * * ?";
        return "0 0/1 * * * ?";
    }

}
